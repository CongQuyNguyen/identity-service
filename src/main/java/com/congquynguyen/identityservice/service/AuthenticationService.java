package com.congquynguyen.identityservice.service;

import com.congquynguyen.identityservice.dto.request.AuthenticationRequest;
import com.congquynguyen.identityservice.dto.request.IntrospectRequest;
import com.congquynguyen.identityservice.dto.response.AuthenticationResponse;
import com.congquynguyen.identityservice.dto.response.IntrospectResponse;
import com.congquynguyen.identityservice.entity.UserEntity;
import com.congquynguyen.identityservice.exception.AppException;
import com.congquynguyen.identityservice.exception.ErrorCode;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.Set;
import java.util.StringJoiner;

@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {

    @NonFinal // Advice insert this into bean IoC
    @Value("${jwt.signerKey}")  // Read a value form .yaml
    protected String SIGNER_KEY;

    UserRepository userRepository;

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

        try {
            JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
            SignedJWT signedJWT = SignedJWT.parse(token);

            // Check experience of token
            Date expTime = signedJWT.getJWTClaimsSet().getExpirationTime();
            var verified = signedJWT.verify(verifier);

            return IntrospectResponse.builder()
                    .valid(verified && expTime.after(new Date()))
                    .build();

        } catch (JOSEException | ParseException e) {
            throw new RuntimeException(e);
        }
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
                .expirationTime(new Date(new Date().getTime() + 1000 * 60 * 60))
                .claim("scope", buildScope(userEntity.getRoles()))
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
    private String buildScope(Set<String> scopes) {
        StringJoiner stringJoiner = new StringJoiner(" ");
        if(!scopes.isEmpty()) {
            scopes.forEach(stringJoiner::add);
        }
        return stringJoiner.toString();
    }
}
