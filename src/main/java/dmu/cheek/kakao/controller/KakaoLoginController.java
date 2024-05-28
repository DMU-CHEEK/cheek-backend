package dmu.cheek.kakao.controller;

import dmu.cheek.kakao.model.KakaoLoginDto;
import dmu.cheek.kakao.model.KakaoLoginResponseDto;
import dmu.cheek.kakao.model.KakaoTokenDto;
import dmu.cheek.member.service.MemberService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;

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
    public ResponseEntity<KakaoLoginResponseDto> loginCallBack(@RequestParam(name = "code") String code,
                                                               HttpServletResponse httpServletResponse) throws IOException {
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
//        String refreshToken = kakaoResponse.getRefresh_token();
//        Integer accessTokenExpireTime = kakaoResponse.getExpires_in();
//        Integer refreshTokenExpireTime = kakaoResponse.getRefresh_token_expires_in();

        KakaoLoginResponseDto kakaoLoginResponseDto = kakaoLoginClient.getkakaoUserInfo(contentType, accessToken);
        if (!memberService.isExistMember(kakaoLoginResponseDto.getKakaoAccount().getEmail()))
            memberService.register(kakaoLoginResponseDto);

        log.info("dtodtodtodtodtodtodtodto: ", kakaoLoginResponseDto.getKakaoAccount().getEmail());
        log.info("!!!!!!!!!!!!!!!!!!!!!!");

        return ResponseEntity.ok(kakaoLoginResponseDto);

//        String redirectUri = UriComponentsBuilder
//                .fromUriString("")
//                .queryParam("accessToken", accessToken)
//                .queryParam("refreshToken", refreshToken)
//                .queryParam("accessTokenExpireTime", accessTokenExpireTime)
//                .queryParam("refreshTokenExpireTime", refreshTokenExpireTime)
//                .build().toUriString();
//
//        httpServletResponse.sendRedirect(redirectUri);


    }

    @PostMapping("/login")
    public ResponseEntity<KakaoLoginDto.Response> loginController(@RequestBody KakaoLoginDto.Request requestDto,
                                                                  HttpServletResponse httpServletResponse) throws ParseException {

        log.info("요청 왓다 !!");
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
