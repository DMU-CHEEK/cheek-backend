package dmu.cheek.global.token.controller;

import dmu.cheek.global.token.dto.AccessTokenResponseDto;
import dmu.cheek.global.token.service.TokenService;
import dmu.cheek.global.util.AuthorizationHeaderUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/token")
@Tag(name = "Token API")
public class TokenController {

    private final TokenService tokenService;

    @PostMapping("/access-token/issue")
    @Operation(summary = "토큰 재발급", description = "accessToken 만료 시 재발급 API")
    public ResponseEntity<AccessTokenResponseDto> createAccessToken(HttpServletRequest httpServletRequest) {
        String authorization = httpServletRequest.getHeader("Authorization");
        AuthorizationHeaderUtils.validateAuthorization(authorization);

        String refreshToken = authorization.split(" ")[1];

        AccessTokenResponseDto accessTokenResponseDto = tokenService.createAccessTokenByRefreshToken(refreshToken);

        return ResponseEntity.ok(accessTokenResponseDto);
    }
}
