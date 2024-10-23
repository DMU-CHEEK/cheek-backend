package dmu.cheek.global.interceptor;

import dmu.cheek.global.config.security.service.CustomUserDetailsService;
import dmu.cheek.global.error.ErrorCode;
import dmu.cheek.global.error.exception.AuthenticationException;
import dmu.cheek.global.token.constant.TokenType;
import dmu.cheek.global.token.service.TokenManager;
import dmu.cheek.global.util.AuthorizationHeaderUtils;
import dmu.cheek.oauth.kakao.client.KakaoLoginClient;
import dmu.cheek.oauth.kakao.dto.KakaoTokenInfoDto;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@Slf4j
public class AuthInterceptor implements HandlerInterceptor {

    @Autowired @Lazy
    private KakaoLoginClient kakaoLoginClient;
    @Autowired
    private TokenManager tokenManager;
    @Autowired
    private CustomUserDetailsService userDetailsService;

    private boolean isSwaggerRequest(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return uri.contains("swagger") || uri.contains("api-docs") || uri.contains("webjars");
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String authorization = request.getHeader("Authorization");
        AuthorizationHeaderUtils.validateAuthorization(authorization);

        String accessToken = authorization.split(" ")[1];
        Claims tokenClaims = tokenManager.getTokenClaims(accessToken);
        String tokenType = tokenClaims.getSubject();

        if (!TokenType.isAccessType(tokenType)) {
            throw new AuthenticationException(ErrorCode.NOT_ACCESS_TOKEN_TYPE);
        }

        // 멤버 ID를 사용하여 사용자 정보를 조회
        Long memberId = tokenClaims.get("memberId", Long.class);
        UserDetails userDetails = userDetailsService.loadUserByMemberId(memberId);

        // SecurityContextHolder에 인증 정보 설정
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return true;
    }



    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private boolean isValidToken(String token) {
        try {
            KakaoTokenInfoDto tokenInfo = kakaoLoginClient.getTokenInfo("Bearer " + token);

            //토큰 만료 여부 검증
            return tokenInfo != null && tokenInfo.getExpires_in() > 0;
        } catch (Exception e) {
            return false;
        }
    }
}
