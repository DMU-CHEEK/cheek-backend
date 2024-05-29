package dmu.cheek.member.service;

import dmu.cheek.kakao.controller.KakaoLoginClient;
import dmu.cheek.kakao.model.KakaoLoginDto;
import dmu.cheek.kakao.model.KakaoLoginResponseDto;
import dmu.cheek.member.model.*;
import dmu.cheek.member.repository.MemberRepository;
import dmu.cheek.s3.model.S3Dto;
import dmu.cheek.s3.service.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.text.SimpleDateFormat;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final S3Service s3Service;

    public boolean isExistMember(String email) {
        return memberRepository.findByEmail(email).isPresent();
    }


    @Transactional
    public void register(KakaoLoginResponseDto kakaoLoginResponseDto) {
        //TODO: modify status and role
        Member member = Member.withEmail()
                .email(kakaoLoginResponseDto.getKakaoAccount().getEmail())
                .build();

        if (member == null)
            log.info("member is null");
        else
            log.info("member is not null");

        memberRepository.save(member);

        log.info("save member: {}", member.getEmail());
    }

    @Transactional
    public void setProfile(ProfileDto profileDto, MultipartFile profilePicture) {
        Member member = findByEmail(profileDto.getEmail());

        S3Dto s3Dto = s3Service.saveFile(profilePicture);
        member.setProfile(profileDto.getNickname(), profileDto.getInformation(), s3Dto.getStoreFileName(), profileDto.getRole());

        log.info("set profile: {}", profileDto.getEmail());
    }

    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email).orElseThrow(RuntimeException::new); //TODO: exception
    }

    public KakaoLoginDto.Response login(KakaoLoginDto.Request requestDto, KakaoLoginResponseDto kakaoLoginResponseDto) throws ParseException {
        String email = kakaoLoginResponseDto.getKakaoAccount().getEmail();
        Member member = memberRepository.findByEmail(email).orElseThrow(RuntimeException::new); //TODO: exception

        log.info("login member: {}", member.getMemberId());

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        return KakaoLoginDto.Response.builder()
                .memberId(member.getMemberId())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .profilePicture(member.getProfilePicture())
                .role(member.getRole())
                .accessToken(requestDto.getAccessToken())
                .accessTokenExpireTime(formatter.parse(requestDto.getAccessTokenExpireTime()))
                .refreshToken(requestDto.getRefreshToken())
                .refreshTokenExpireTime(formatter.parse(requestDto.getRefreshTokenExpireTime()))
                .build();

    }

}