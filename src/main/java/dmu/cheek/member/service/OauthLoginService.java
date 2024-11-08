package dmu.cheek.member.service;

import dmu.cheek.global.token.dto.JwtTokenDto;
import dmu.cheek.global.token.service.TokenManager;
import dmu.cheek.member.constant.MemberType;
import dmu.cheek.member.constant.Role;
import dmu.cheek.member.constant.Status;
import dmu.cheek.member.model.Member;
import dmu.cheek.member.model.OauthLoginDto;
import dmu.cheek.oauth.model.OAuthAttributes;
import dmu.cheek.oauth.service.SocialLoginApiService;
import dmu.cheek.oauth.service.SocialLoginApiServiceFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
public class OauthLoginService {

    private final MemberService memberService;
    private final TokenManager tokenManager;

    @Transactional
    public OauthLoginDto.Response oauthLogin(String accessToken, MemberType memberType) {
        SocialLoginApiService socialLoginApiService = SocialLoginApiServiceFactory.getSocialLoginApiService(memberType);
        OAuthAttributes userInfo = socialLoginApiService.getUserInfo(accessToken);

        JwtTokenDto jwtTokenDto;
        boolean isProfileComplete;

        Optional<Member> existMember = memberService.findByEmailAndMemberType(userInfo.getEmail(), userInfo.getMemberType());
        long memberId;

        Member member;
        if (existMember.isEmpty()) { //신규 가입
            member = Member.join()
                    .memberType(memberType)
                    .email(userInfo.getEmail())
                    .role(Role.MENTEE)
                    .build();

            member = memberService.register(member); //가입
        } else { //기존 회원
            member = existMember.get();
        }

        // 지금 중요한건 저 조건문 위 아래로 다찍어봤는데 생성 로직에서만 안들어갔다는거네웅 빌더문제일것같은데 아무리봐도
        // 다른건 모르겠어 일단 잠깐만


        //토큰 생성
        memberId = member.getMemberId();
        isProfileComplete = StringUtils.hasText(member.getNickname()) && StringUtils.hasText(member.getInformation());
        jwtTokenDto = tokenManager.createJwtTokenDto(member.getMemberId(), member.getRole());
        member.updateRefreshToken(jwtTokenDto);

        return OauthLoginDto.Response.of(memberId, jwtTokenDto, isProfileComplete);
    }


}
