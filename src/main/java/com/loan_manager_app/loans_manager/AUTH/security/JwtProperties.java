package com.loan_manager_app.loans_manager.AUTH.security;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    private String secretKey;
    private long accessTokenExpiration;
    private long refreshTokenExpiration;

    private Cookie cookie = new Cookie();
    @Getter
    @Setter
    public static class Cookie {
        private String path;
        private boolean secure;
        private String sameSite;
    }
}
