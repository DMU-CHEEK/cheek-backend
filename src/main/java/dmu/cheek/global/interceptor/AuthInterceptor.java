package dmu.cheek.global.interceptor;

import dmu.cheek.kakao.controller.KakaoLoginClient;
import dmu.cheek.kakao.model.KakaoTokenInfoDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Autowired @Lazy
    private KakaoLoginClient kakaoLoginClient;

    private boolean isSwaggerRequest(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return uri.contains("swagger") || uri.contains("api-docs") || uri.contains("webjars");
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (isSwaggerRequest(request))
            return true;

        String token = getTokenFromRequest(request);
        if (token == null || !isValidToken(token)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or missing token");
            return false;
        }

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
