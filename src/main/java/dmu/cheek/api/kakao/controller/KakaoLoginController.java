package dmu.cheek.api.kakao.controller;

import dmu.cheek.api.kakao.model.KakaoLoginDto;
import dmu.cheek.api.kakao.model.KakaoLoginResponseDto;
import dmu.cheek.api.kakao.model.KakaoTokenDto;
import dmu.cheek.api.member.service.MemberService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

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

        log.info("contentType: ", contentType);

        KakaoTokenDto.Request kakaoTokenRequestDto = KakaoTokenDto.Request.builder()
                .client_id(clientId)
                .grant_type(authorizationGrantType)
                .code(code)
                .redirect_uri(redirectUri)
                .client_secret(clientSecret)
                .build();

        KakaoTokenDto.Response kakaoResponse = kakaoTokenClient.requestKakaoToken(contentType, kakaoTokenRequestDto);

        log.info("kakaoResponse: ", kakaoResponse.getAccess_token());

        String accessToken = kakaoResponse.getAccess_token();
        String refreshToken = kakaoResponse.getRefresh_token();
        Integer accessTokenExpireTime = kakaoResponse.getExpires_in();
        Integer refreshTokenExpireTime = kakaoResponse.getRefresh_token_expires_in();

        KakaoLoginResponseDto kakaoLoginResponseDto = kakaoLoginClient.getkakaoUserInfo(contentType, accessToken);
        if (!memberService.isExistMember(kakaoLoginResponseDto.getKakaoAccount().getEmail()))
            memberService.register(kakaoLoginResponseDto);

        log.info("dtodtodtodtodtodtodtodto: ", kakaoLoginResponseDto.getKakaoAccount().getEmail());
        log.info("!!!!!!!!!!!!!!!!!!!!!!");

        String redirectUri = UriComponentsBuilder
                .fromUriString("")
                .queryParam("accessToken", accessToken)
                .queryParam("refreshToken", refreshToken)
                .queryParam("accessTokenExpireTime", accessTokenExpireTime)
                .queryParam("refreshTokenExpireTime", refreshTokenExpireTime)
                .build().toUriString();

        httpServletResponse.sendRedirect(redirectUri);
    }

    @PostMapping("/login")
    public ResponseEntity<KakaoLoginDto.Response> loginController(@RequestBody KakaoLoginDto.Request requestDto,
                                                                  HttpServletResponse httpServletResponse) {

        String authorization = httpServletResponse.getHeader("Authorization");

        if (!StringUtils.hasText(authorization))
            throw new RuntimeException(); //TODO: exception

        String[] splitHeader = authorization.split(" ");

        if (splitHeader.length < 2 || ("Bearer".equals(splitHeader[0])))
            throw new RuntimeException(); //TODO: exception

        String accessToken = authorization.split(" ")[1];

        KakaoLoginDto.Response responseDto = memberService.login(requestDto, accessToken);

        return ResponseEntity.ok(responseDto);
    }
}
