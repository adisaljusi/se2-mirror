package ch.zhaw.freelance4u.security;

import java.util.List;
import java.util.Map;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;

@TestConfiguration
public class TestSecurityConfig {

    @Bean
    @Primary
    public JwtDecoder jwtDecoder() {
        return token -> {
            if (token.equals("admin")) {
                return Jwt.withTokenValue("admin")
                        .header("alg", "none")
                        .claim("user_roles", List.of("admin"))
                        .build();
            } else if (token.equals("user")) {
                return Jwt.withTokenValue("user")
                        .header("alg", "none")
                        .claim("user_roles", List.of("user"))
                        .build();
            }
            throw new IllegalArgumentException("Invalid token");
        };
    }
}
