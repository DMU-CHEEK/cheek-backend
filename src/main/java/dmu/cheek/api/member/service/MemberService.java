package dmu.cheek.api.member.service;

import dmu.cheek.api.file.service.FileService;
import dmu.cheek.api.kakao.controller.KakaoLoginClient;
import dmu.cheek.api.kakao.model.KakaoLoginDto;
import dmu.cheek.api.kakao.model.KakaoLoginResponseDto;
import dmu.cheek.api.member.model.Member;
import dmu.cheek.api.member.model.ProfileDto;
import dmu.cheek.api.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final KakaoLoginClient kakaoLoginClient;
    private final FileService fileService;

    public boolean isExistMember(String email) {
        return memberRepository.findByEmail(email) == null ? false : true;
    }


    @Transactional
    public void register(KakaoLoginResponseDto kakaoLoginResponseDto) {
        Member member = Member.withEmail()
                .email(kakaoLoginResponseDto.getKakaoAccount().getEmail())
                .build();

        memberRepository.save(member);

        log.info("save member: {}", member.getEmail());
    }

    @Transactional
    public void setProfile(ProfileDto profileDto, MultipartFile profilePicture) throws IOException {
        Member member = findByEmail(profileDto.getEmail());

        String storeFileName = fileService.storeFile(profilePicture);
        member.setProfile(profileDto.getNickname(), profileDto.getInformation(), storeFileName, profileDto.getRole());

        log.info("set profile: {}", profileDto.getEmail());
    }

    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email).orElseThrow(RuntimeException::new); //TODO: exception
    }

    public KakaoLoginDto.Response login(KakaoLoginDto.Request requestDto, String accessToken) {
        String contentType = "application/x-www-form-urlencoded/charset=utf-8";
        KakaoLoginResponseDto kakaoLoginResponseDto = kakaoLoginClient.getkakaoUserInfo(contentType, accessToken);

        String email = kakaoLoginResponseDto.getKakaoAccount().getEmail();
        Member member = memberRepository.findByEmail(email).orElseThrow(RuntimeException::new); //TODO: exception

        log.info("login member: {}", member.getMemberId());

        return KakaoLoginDto.Response.builder()
                .memberId(member.getMemberId())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .profile(member.getProfilePicture())
                .role(member.getRole())
                .accessToken(accessToken)
                .accessTokenExpireTime(requestDto.getAccessTokenExpireTime())
                .refreshToken(requestDto.getRefreshToken())
                .refreshTokenExpireTime(requestDto.getRefreshTokenExpireTime())
                .build();

    }

}