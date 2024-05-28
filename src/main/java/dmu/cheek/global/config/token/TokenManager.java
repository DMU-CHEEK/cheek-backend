package dmu.cheek.global.config.token;

import dmu.cheek.member.model.Role;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.nio.charset.StandardCharsets;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class TokenManager {

    @Value("${jwt.access-token-validity-in-seconds}")
    private final long accessTokenExpirationTime;

    @Value("${jwt.refresh-token-validity-in-seconds}")
    private final long refreshTokenExpirationTime;

    @Value("${jwt.secret}")
    private final String tokenSecret;

    public String createAccessToken(long memberId, Role role) {
        ZonedDateTime now = ZonedDateTime.now().withZoneSameInstant(ZoneId.of("Asia/Seoul"));
        ZonedDateTime tokenValidity = now.withZoneSameInstant(ZoneId.of("Asia/Seoul")).plusSeconds(accessTokenExpirationTime);

        String accessToken = Jwts.builder()
                .setSubject(TokenType.ACCESS.name())
                .setIssuedAt(Date.from(now.toInstant()))
                .setExpiration(Date.from(tokenValidity.toInstant()))
                .claim("id", memberId)
                .claim("role", role)
                .signWith(SignatureAlgorithm.HS512, tokenSecret.getBytes(StandardCharsets.UTF_8))
                .setHeaderParam("type", "JWT")
                .compact();

        return accessToken;
    }

    public String createRefreshToken(long memberId) {
        ZonedDateTime now = ZonedDateTime.now().withZoneSameInstant(ZoneId.of("Asia/Seoul"));
        ZonedDateTime tokenValidity = now.withZoneSameInstant(ZoneId.of("Asia/Seoul")).plusSeconds(refreshTokenExpirationTime);

        String refreshToken = Jwts.builder()
                .setSubject(TokenType.REFRESH.name())
                .setIssuedAt(Date.from(now.toInstant()))
                .setExpiration(Date.from(tokenValidity.toInstant()))
                .claim("id", memberId)
                .signWith(SignatureAlgorithm.HS512, tokenSecret.getBytes(StandardCharsets.UTF_8))
                .setHeaderParam("type", "JWT")
                .compact();

        return refreshToken;
    }

    public boolean isValidToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(tokenSecret.getBytes(StandardCharsets.UTF_8))
                    .parseClaimsJws(token); //token parsing
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        }

        return false;
    }

    public Optional<Claims> getTokenClaims(String token) {
        Optional<Claims> claims = Optional.empty();

        try {
            Claims parsedClaims = Jwts.parser()
                    .setSigningKey(tokenSecret.getBytes(StandardCharsets.UTF_8))
                    .parseClaimsJws(token).getBody();
            claims = Optional.of(parsedClaims);
        } catch (Exception e){
            log.info("유효하지 않은 토큰입니다.", e);
        }

        return claims;
    }

}
