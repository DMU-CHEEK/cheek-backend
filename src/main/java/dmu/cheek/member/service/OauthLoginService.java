package dmu.cheek.member.service;

import dmu.cheek.global.token.dto.JwtTokenDto;
import dmu.cheek.global.token.service.TokenManager;
import dmu.cheek.member.constant.MemberType;
import dmu.cheek.member.constant.Role;
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

        Optional<Member> existMember = memberService.findByEmailOrNull(userInfo.getEmail());
        long memberId;

        if (existMember.isEmpty()) { //신규 가입
            Member member = Member.joinBuilder()
                    .email(userInfo.getEmail())
                    .role(Role.MENTEE)
                    .memberType(memberType)
                    .build();

            member = memberService.register(member); //가입
            memberId = member.getMemberId();
            isProfileComplete = StringUtils.hasText(member.getNickname()) && StringUtils.hasText(member.getInformation());

            //토큰 생성
            jwtTokenDto = tokenManager.createJwtTokenDto(member.getMemberId(), member.getRole());
            member.updateRefreshToken(jwtTokenDto);

        } else { //기존 회원
            Member member = existMember.get();
            memberId = member.getMemberId();
            isProfileComplete = StringUtils.hasText(member.getNickname()) && StringUtils.hasText(member.getInformation());

            //토큰 생성
            jwtTokenDto = tokenManager.createJwtTokenDto(member.getMemberId(), member.getRole());
            member.updateRefreshToken(jwtTokenDto);
        }

        return OauthLoginDto.Response.of(memberId, jwtTokenDto, isProfileComplete);
    }


}
