package dmu.cheek.oauth.kakao.service;

import dmu.cheek.global.token.constant.GrantType;
import dmu.cheek.member.constant.MemberType;
import dmu.cheek.oauth.kakao.client.KakaoLoginClient;
import dmu.cheek.oauth.kakao.dto.KakaoLoginResponseDto;
import dmu.cheek.oauth.model.OAuthAttributes;
import dmu.cheek.oauth.service.SocialLoginApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class KakaoLoginApiServiceImpl implements SocialLoginApiService {

    private final KakaoLoginClient kakaoLoginClient;
    private final String CONTENT_TYPE = "application/x-www-form-urlencoded;charset=utf-8";


    @Override
    public OAuthAttributes getUserInfo(String accessToken) {
        KakaoLoginResponseDto kakaoUserInfoResponse = kakaoLoginClient.getKakaoUserInfo(CONTENT_TYPE,
                GrantType.BEARER.getGrantType() + " " + accessToken);

        KakaoLoginResponseDto.KakaoAccount kakaoAccount = kakaoUserInfoResponse.getKakaoAccount();
        String email = kakaoAccount.getEmail();

        return OAuthAttributes.builder()
                .email(!StringUtils.hasText(email) ? kakaoUserInfoResponse.getId() : email)
                .memberType(MemberType.KAKAO)
                .build();
    }
}
