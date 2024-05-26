package dmu.cheek.api.kakao.controller;

import dmu.cheek.api.kakao.model.KakaoLoginResponseDto;
import dmu.cheek.api.kakao.model.KakaoTokenDto;
import dmu.cheek.api.member.service.MemberService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class KakaoLoginController {

    private final KakaoTokenClient kakaoTokenClient;
    private final KakaoLoginClient kakaoLoginClient;
    private final MemberService memberService;

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.kakao.authorization-grant-type}")
    private String authorizationGrantType;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String redirectUri;

    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String clientSecret;


    @GetMapping("/oauth/kakao/callback")
    public void loginCallBack(String code, HttpServletResponse httpServletResponse) throws IOException {
        String contentType = "application/x-www-form-urlencoded/charset=utf-8";

        KakaoTokenDto.Request kakaoTokenRequestDto = KakaoTokenDto.Request.builder()
                .client_id(clientId)
                .grant_type(authorizationGrantType)
                .code(code)
                .redirect_uri(redirectUri)
                .client_secret(clientSecret)
                .build();


        KakaoTokenDto.Response kakaoTokenResponseDto = kakaoTokenClient.requestKakaoToken(contentType, kakaoTokenRequestDto); //토큰 요청

        String accessToken = kakaoTokenResponseDto.getAccess_token();
        String refreshToken = kakaoTokenResponseDto.getRefresh_token();
        Integer accessTokenExpireTime = kakaoTokenResponseDto.getExpires_in();
        Integer refreshTokenExpireTime = kakaoTokenResponseDto.getRefresh_token_expires_in();

        KakaoLoginResponseDto kakaoLoginResponseDto = kakaoLoginClient.getkakaoUserInfo();
        if (!memberService.isExistMember(kakaoLoginResponseDto.getKakaoAccount().getEmail()))
            memberService.register(kakaoLoginResponseDto);


        String redirectUri = UriComponentsBuilder
                .fromUriString("") //TODO: uri 변경
                .queryParam("accessToken", accessToken)
                .queryParam("refreshToken", refreshToken)
                .queryParam("accessTokenExpireTime", accessTokenExpireTime)
                .queryParam("refreshTokenExpireTime", refreshTokenExpireTime)
                .queryParam("email", kakaoLoginResponseDto.getKakaoAccount().getEmail())
                .build().toUriString();

        httpServletResponse.sendRedirect(redirectUri);
    }
}
