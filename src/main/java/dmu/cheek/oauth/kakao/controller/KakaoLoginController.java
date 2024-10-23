package dmu.cheek.oauth.kakao.controller;

import dmu.cheek.oauth.kakao.dto.KakaoLoginResponseDto;
import dmu.cheek.oauth.kakao.dto.KakaoTokenDto;
import dmu.cheek.oauth.kakao.dto.KakaoTokenInfoDto;
import dmu.cheek.member.service.MemberService;
import dmu.cheek.oauth.kakao.client.KakaoLoginClient;
import dmu.cheek.oauth.kakao.client.KakaoTokenClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/oauth")
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

    @GetMapping("/kakao/callback")
    public ResponseEntity<KakaoLoginResponseDto> loginCallBack(@RequestParam(name = "code") String code) throws IOException {
        String contentType = "application/x-www-form-urlencoded/charset=utf-8";

        log.info("contentType: ", contentType);

        KakaoTokenDto.Request kakaoTokenRequestDto = KakaoTokenDto.Request.builder()
                .client_id(clientId)
                .grant_type(authorizationGrantType)
                .code(code)
                .redirect_uri(redirectUri)
                .build();

        KakaoTokenDto.Response kakaoResponse = kakaoTokenClient.requestKakaoToken(contentType, kakaoTokenRequestDto);

        log.info("kakaoResponse: ", kakaoResponse.getAccess_token());

        String accessToken = kakaoResponse.getAccess_token();
        String refreshToken = kakaoResponse.getRefresh_token();
        Integer accessTokenExpireTime = kakaoResponse.getExpires_in();
        Integer refreshTokenExpireTime = kakaoResponse.getRefresh_token_expires_in();

        KakaoLoginResponseDto kakaoLoginResponseDto = kakaoLoginClient.getKakaoUserInfo(contentType, accessToken);

//        if (!memberService.isExistMember(kakaoLoginResponseDto.getKakaoAccount().getEmail()))
//            memberService.register(kakaoLoginResponseDto);

//        response.sendRedirect(UriComponentsBuilder
//                .fromUriString("http://localhost:3000/kakao/login/callback")
//                .queryParam("accessToken", accessToken)
//                .queryParam("refreshToken", refreshToken)
//                .queryParam("accessTokenExpireTime", accessTokenExpireTime)
//                .queryParam("refreshTokenExpireTime", refreshTokenExpireTime)
//                .build().toUriString());

        return ResponseEntity.ok(kakaoLoginResponseDto);
    }

    @GetMapping("/token")
    public ResponseEntity<?> getKakaoTokenInfo(@RequestParam String accessToken) {
        KakaoTokenInfoDto tokenInfo = kakaoLoginClient.getTokenInfo(accessToken);
        return ResponseEntity.ok(tokenInfo);
    }

//    @PostMapping("/login")
//    public ResponseEntity<KakaoLoginDto.Response> loginController(@RequestBody KakaoLoginDto.Request requestDto,
//                                                                  HttpServletRequest httpServletResponse) throws ParseException {
//
//        String authorization = httpServletResponse.getHeader("Authorization");
//
//        log.info("Authorization: {}", authorization);
//        if (!StringUtils.hasText(authorization))
//            throw new RuntimeException("authorization is null"); //TODO: exception
//
//        String[] splitHeader = authorization.split(" ");
//
//        log.info("Header: {}", splitHeader[0]);
//
//        if (splitHeader.length < 2)
//            throw new RuntimeException("splitHeader.length < 2"); //TODO: exception
//        if (!"Bearer".equals(splitHeader[0]))
//            throw new RuntimeException("! \"Bearer\".equals(splitHeader[0]");
//
//        String accessToken = authorization.split(" ")[1];
//
//        KakaoLoginDto.Response responseDto = memberService.login(requestDto, accessToken);
//
//        log.info("login successful: {}", responseDto.getEmail());
//
//        return ResponseEntity.ok(responseDto);
//    }

}
