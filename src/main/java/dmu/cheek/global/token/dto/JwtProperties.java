package dmu.cheek.global.token.dto;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("jwt")
public class JwtProperties {

    private String issuer;

    private String secretKey;

    private String accessTokenExpirationTime;

    private String refreshTokenExpirationTime;
}
