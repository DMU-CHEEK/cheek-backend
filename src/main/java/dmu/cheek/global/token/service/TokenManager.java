package dmu.cheek.global.token.service;

import dmu.cheek.global.error.ErrorCode;
import dmu.cheek.global.error.exception.AuthenticationException;
import dmu.cheek.global.error.exception.BusinessException;
import dmu.cheek.global.token.constant.GrantType;
import dmu.cheek.global.token.constant.TokenType;
import dmu.cheek.global.token.dto.JwtTokenDto;
import dmu.cheek.member.constant.Role;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Slf4j
@RequiredArgsConstructor
public class TokenManager {

    private final String accessTokenExpirationTime;
    private final String refreshTokenExpirationTime;
    private final String  tokenSecret;

    public JwtTokenDto createJwtTokenDto(long memberId, Role role) {
        Date accessTokenExpireTime = createAccessTokenExpireTime();
        Date refreshTokenExpireTime = createRefreshTokenExpireTime();

        String accessToken = createAccessToken(memberId, role, accessTokenExpireTime);
        String refreshToken = createRefreshToken(memberId, refreshTokenExpireTime);

        return JwtTokenDto.builder()
                .grantType(GrantType.BEARER.getGrantType() )
                .accessToken(accessToken)
                .accessTokenExpireTime(accessTokenExpireTime)
                .refreshToken(refreshToken)
                .refreshTokenExpireTime(refreshTokenExpireTime )
                .build();
    }

    public Date createAccessTokenExpireTime() {
        return new Date(System.currentTimeMillis() + Long.parseLong(accessTokenExpirationTime));
    }

    public Date createRefreshTokenExpireTime() {
        return new Date(System.currentTimeMillis() + Long.parseLong(refreshTokenExpirationTime));
    }

    public String createAccessToken(long memberId, Role role, Date expirationTime) {
        String accessToken = Jwts.builder()
                .setSubject(TokenType.ACCESS.name()) //토큰 제목
                .setIssuedAt(new Date()) //발급 시간
                .setExpiration(expirationTime) //만료 시간
                .claim("memberId", memberId)
                .claim("role", role)
                .signWith(SignatureAlgorithm.HS512, tokenSecret.getBytes(StandardCharsets.UTF_8))
                .setHeaderParam("type", "JWT")
                .compact();

        return accessToken;
    }

    public String createRefreshToken(long memberId, Date expirationTime) {
        String refreshToken = Jwts.builder()
                .setSubject(TokenType.REFRESH.name()) //토큰 제목
                .setIssuedAt(new Date()) //발급 시간
                .setExpiration(expirationTime) //만료 시간
                .claim("memberId", memberId)
                .signWith(SignatureAlgorithm.HS512, tokenSecret.getBytes(StandardCharsets.UTF_8))
                .setHeaderParam("type", "JWT")
                .compact();

        return refreshToken;
    }

    public void validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(tokenSecret.getBytes(StandardCharsets.UTF_8))
                    .parseClaimsJws(token);
        } catch (ExpiredJwtException e) {
            log.info("토큰이 만료되었습니다.", e);
            throw new AuthenticationException(ErrorCode.EXPIRED_TOKEN);
        } catch (MalformedJwtException e) {
            log.info("토큰 형식이 잘못되었습니다.", e);
            throw new AuthenticationException(ErrorCode.MALFORMED_TOKEN);
        } catch (IllegalArgumentException e) {
            log.info("토큰을 찾을 수 없습니다.", e);
            throw new AuthenticationException(ErrorCode.TOKEN_NOT_FOUND);
        } catch (Exception e) {
            log.info("유효하지 않은 토큰입니다.");
            throw new AuthenticationException(ErrorCode.INVALID_TOKEN);
        }
    }

    public Claims getTokenClaims(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(tokenSecret.getBytes(StandardCharsets.UTF_8))
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            log.info("토큰이 만료되었습니다.", e);
            throw new AuthenticationException(ErrorCode.EXPIRED_TOKEN);
        } catch (MalformedJwtException e) {
            log.info("토큰 형식이 잘못되었습니다.", e);
            throw new AuthenticationException(ErrorCode.MALFORMED_TOKEN);
        } catch (IllegalArgumentException e) {
            log.info("토큰을 찾을 수 없습니다.", e);
            throw new AuthenticationException(ErrorCode.TOKEN_NOT_FOUND);
        } catch (Exception e) {
            log.info("유효하지 않은 토큰입니다.");
            throw new AuthenticationException(ErrorCode.INVALID_TOKEN);
        }
    }
}