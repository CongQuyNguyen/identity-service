package com.congquynguyen.identityservice.configuration;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${jwt.signerKey}")
    private String signerKey;

    @Autowired
    private JwtDecoderCustom jwtDecoder;

    private final String[] PUBLIC_ENDPOINT = {
            "auth/login",
            "auth/introspect",
            "auth/logout",
            "auth/refresh",
            "permissions",
            "users",
            "address"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Thêm CORS
                .csrf(AbstractHttpConfigurer::disable)  // Ẩn đi csrf (một phương thức chống tấn công csrf)
                .authorizeRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers(HttpMethod.POST, PUBLIC_ENDPOINT).permitAll()  // Tất cả đều được accept
                        .requestMatchers(HttpMethod.GET, PUBLIC_ENDPOINT).permitAll()
                        // .requestMatchers(HttpMethod.GET, "/users").hasRole(Role.ADMIN.name())   // Chỉ ADMIN mới được vào
                        .anyRequest()   // Tất cả các cái khác phải được authenticated thì mới được accept
                        .authenticated());

        // Config for valid token generated to decode token from OAuth (in header)
        http
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwtConfigurer -> jwtConfigurer
                                .decoder(jwtDecoder)
                                .jwtAuthenticationConverter(jwtAuthenticationConverter()))
                        .authenticationEntryPoint(new JWTAuthenticationEntryPointConfig()));    // Config cái lỗi bắt ở filter
        return http.build();
    }

    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("http://localhost:3000"); // Cho phép React domain
        corsConfiguration.addAllowedMethod("*"); // Cho phép tất cả các phương thức
        corsConfiguration.addAllowedHeader("*"); // Cho phép tất cả header
        corsConfiguration.setAllowCredentials(true); // Nếu cần cookie, đặt true

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    // Customize authority SCOPE to ROLE
    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedConverter = new JwtGrantedAuthoritiesConverter();
        grantedConverter.setAuthorityPrefix("");

        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(grantedConverter);
        return converter;
    }

}
