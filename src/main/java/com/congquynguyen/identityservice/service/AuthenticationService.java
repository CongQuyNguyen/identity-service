package com.congquynguyen.identityservice.service;

import com.congquynguyen.identityservice.dto.request.AuthenticationRequest;
import com.congquynguyen.identityservice.dto.request.IntrospectRequest;
import com.congquynguyen.identityservice.dto.request.LogoutRequest;
import com.congquynguyen.identityservice.dto.request.RefreshRequest;
import com.congquynguyen.identityservice.dto.response.AuthenticationResponse;
import com.congquynguyen.identityservice.dto.response.IntrospectResponse;
import com.congquynguyen.identityservice.entity.TokenValidationEntity;
import com.congquynguyen.identityservice.entity.UserEntity;
import com.congquynguyen.identityservice.exception.AppException;
import com.congquynguyen.identityservice.exception.ErrorCode;
import com.congquynguyen.identityservice.repository.TokenValidationRepository;
import com.congquynguyen.identityservice.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {

    @NonFinal // Advice insert this into bean IoC
    @Value("${jwt.signerKey}")  // Read a value form .yaml
    protected String SIGNER_KEY;

    @NonFinal
    @Value("${jwt.valid-duration}")
    protected long VALID_DURATION;

    @NonFinal
    @Value("${jwt.refreshable-duration}")
    protected long REFRESHABLE_DURATION;

    UserRepository userRepository;

    TokenValidationRepository tokenValidationRepository;

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        var user = userRepository.findByUsername(authenticationRequest.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean authenticated = passwordEncoder
                .matches(authenticationRequest.getPassword(), user.getPassword());
        if(!authenticated)
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        // Generate token if authenticated is success
        var token = generateToken(user);
        return AuthenticationResponse.builder()
                .token(token)
                .isAuthenticated(true)
                .build();
    }

    // Method introspect (verify token)
    public IntrospectResponse introspect(IntrospectRequest introspectRequest) {
        String token = introspectRequest.getToken();
        var isValid = true;

        try {
            verifyToken(token, false);
        } catch (AppException e) {
            isValid = false;
        }

        return IntrospectResponse.builder()
                .valid(isValid)
                .build();
    }

    // Make a token
    private String generateToken(UserEntity userEntity) {
        // Header
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);

        // Payload (Data in body - Claim)
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(userEntity.getUsername())
                .issuer("congquynguyen")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(VALID_DURATION, ChronoUnit.SECONDS).toEpochMilli()
                ))
                .claim("scope", buildScope(userEntity))
                .jwtID(UUID.randomUUID().toString())    // Random id cho user
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(jwsHeader, payload);

        // Sign token: Use MAC signer, using public and private key will be introduced
        // in other topic
        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }

    // Build a scope from user to attach into token
    private String buildScope(UserEntity userEntity) {
        StringJoiner stringJoiner = new StringJoiner(" ");

        // Gán role, mỗi role gán thêm các permission
        if (!CollectionUtils.isEmpty(userEntity.getRoles())) {
            userEntity.getRoles().forEach(role -> {
                stringJoiner.add("ROLE_" + role.getName());
                if (!CollectionUtils.isEmpty(role.getPermissions())) {
                    role.getPermissions().forEach(permission -> {
                        stringJoiner.add(permission.getName());
                    });
                }
            });
        }

        return stringJoiner.toString();
    }

    // Hàm logout với jwt
    public void logout(LogoutRequest logoutRequest) throws ParseException {

        try {
            // Đọc trong docx
            SignedJWT signedJWT = verifyToken(logoutRequest.getToken(), true);

            String jit = signedJWT.getJWTClaimsSet().getJWTID();
            Date exp = signedJWT.getJWTClaimsSet().getExpirationTime();

            // Lưu thông tin xuống db
            TokenValidationEntity token = TokenValidationEntity.builder()
                    .id(jit)
                    .expiryDate(exp)
                    .build();
            tokenValidationRepository.save(token);
        } catch (AppException e) {
            log.info("Token hết hiệu lực");
        }

    }

    // Hàm lấy thông tin từ token và verify
    private SignedJWT verifyToken(String token, boolean isRefresh) {
        try {
            JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
            SignedJWT signedJWT = SignedJWT.parse(token);

            Date expTime = (isRefresh)
                    ? new Date(signedJWT
                        .getJWTClaimsSet()
                        .getIssueTime()
                        .toInstant()
                        .plus(REFRESHABLE_DURATION, ChronoUnit.SECONDS)
                        .toEpochMilli())
                    : signedJWT.getJWTClaimsSet().getExpirationTime();
            var verified = signedJWT.verify(verifier);

            if (!verified || !expTime.after(new Date()))
                throw new AppException(ErrorCode.UNAUTHENTICATED);

            // Kiểm tra token đã logout chưa
            String jti = signedJWT.getJWTClaimsSet().getJWTID();
            var isExits = tokenValidationRepository.existsById(jti);
            if (isExits)
                throw new AppException(ErrorCode.UNAUTHENTICATED);

            return signedJWT;

        } catch (ParseException | JOSEException e) {
            throw new RuntimeException(e);
        }
    }

    // Hàm refresh token
    public AuthenticationResponse refreshToken(RefreshRequest refreshRequest) throws ParseException {

        // Lấy ra thông tin của JWTSigned thông qua việc check xem token còn hiệu lực hay không
        SignedJWT signedJWT = verifyToken(refreshRequest.getToken(), true);

        // Build và logout token hiện tại
        var jit = signedJWT.getJWTClaimsSet().getJWTID();
        var expTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        TokenValidationEntity tokenInvalid = TokenValidationEntity.builder()
                .id(jit)
                .expiryDate(expTime)
                .build();
        tokenValidationRepository.save(tokenInvalid);

        // Cập nhật token mới (refresh token)
        var userName = signedJWT.getJWTClaimsSet().getSubject();
        var user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        var token = generateToken(user);
        return AuthenticationResponse.builder()
                .isAuthenticated(true)
                .token(token)
                .build();
    }
}
