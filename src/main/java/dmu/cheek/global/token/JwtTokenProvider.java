package dmu.cheek.global.token;

import dmu.cheek.global.error.ErrorCode;
import dmu.cheek.global.error.exception.BusinessException;
import dmu.cheek.global.token.dto.JwtProperties;
import dmu.cheek.global.token.dto.TokenDetail;
import dmu.cheek.member.model.Member;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final JwtProperties jwtProperties;

    public TokenDetail generateToken(Member member, Duration expiredAt, TokenType type){

        Date now = new Date();
        Date expiry = new Date(now.getTime() + expiredAt.toMillis());

        String token = createToken(member, expiry, type);
        LocalDateTime expiryDateTime = expiry.toInstant().atZone(ZoneId.of("Asia/Seoul")).toLocalDateTime();

        return new TokenDetail(token, expiryDateTime);
    }

    private String createToken(Member member, Date expiry, TokenType type) {

        if (TokenType.isAccessType(type.name()))
            return createAccessToken(member, expiry);

        return createRefreshToken(member, expiry);
    }

    private String createAccessToken(Member member, Date expiry) {

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setHeaderParam("TokenType", TokenType.ACCESS.name())
                .setIssuer(jwtProperties.getIssuer())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(expiry)
                .setSubject(member.getEmail())
                .claim("id", member.getMemberId())
                .claim("nickname", member.getNickname())
                .claim("role", member.getRole())
                .signWith(SignatureAlgorithm.ES256, jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8))
                .compact();
    }

    private String createRefreshToken(Member member, Date expiry) {

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setHeaderParam("TokenType", TokenType.REFRESH.name())
                .setIssuer(jwtProperties.getIssuer())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(expiry)
                .setSubject(member.getEmail())
                .claim("id", member.getMemberId())
                .signWith(SignatureAlgorithm.ES256, jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8))
                .compact();
    }

    public boolean validateAccessToken(String accessToken) {
        try {
            Jwts.parser()
                    .setSigningKey(jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8))
                    .parseClaimsJws(accessToken)
                    .getBody();
            return true;
        } catch (MalformedJwtException e) {
            log.info("형식에 맞지 않는 token");
            throw new BusinessException(ErrorCode.MALFORMED_TOKEN);
        } catch (ExpiredJwtException e) {
            log.info("만료된 token");
            throw new BusinessException(ErrorCode.EXPIRED_TOKEN);
       } catch (Exception e) {
            log.info("유효하지 않는 token");
            throw new BusinessException(ErrorCode.INVALID_TOKEN);
        }
    }
}