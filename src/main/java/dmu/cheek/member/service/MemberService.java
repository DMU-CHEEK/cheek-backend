package dmu.cheek.member.service;

import dmu.cheek.global.error.ErrorCode;
import dmu.cheek.global.error.exception.BusinessException;
import dmu.cheek.kakao.model.KakaoLoginDto;
import dmu.cheek.kakao.model.KakaoLoginResponseDto;
import dmu.cheek.member.converter.MemberConverter;
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
    private final MemberConverter memberConverter;

    public boolean isExistMember(String email) {
        return memberRepository.findByEmail(email).isPresent();
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
    public void setProfile(ProfileDto profileDto, MultipartFile profilePicture) {
        Member member = findByEmail(profileDto.getEmail());

        if (profileDto != null) {
            S3Dto s3Dto = s3Service.saveFile(profilePicture);
            member.setProfilePicture(s3Dto.getStoreFileName());
        }

        member.setProfile(profileDto.getNickname(), profileDto.getInformation(), profileDto.getRole(), Status.COMPLETE);


        log.info("set profile: {}", profileDto.getEmail());
    }

    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email).orElseThrow(
                () -> new BusinessException(ErrorCode.EMAIL_NOT_FOUND));
    }

    public KakaoLoginDto.Response login(KakaoLoginDto.Request requestDto, KakaoLoginResponseDto kakaoLoginResponseDto) throws ParseException {
        String email = kakaoLoginResponseDto.getKakaoAccount().getEmail();
        Member member = memberRepository.findByEmail(email).orElseThrow(
                () -> new BusinessException(ErrorCode.EMAIL_NOT_FOUND));

        log.info("login member: {}", member.getMemberId());

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String profilePictureUrl = (member.getProfilePicture() != null) ? s3Service.getResourceUrl(member.getProfilePicture()) : null;

        return KakaoLoginDto.Response.builder()
                .memberId(member.getMemberId())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .profilePicture(profilePictureUrl)
                .information(member.getInformation())
                .role(member.getRole())
                .accessToken(requestDto.getAccessToken())
                .accessTokenExpireTime(formatter.parse(requestDto.getAccessTokenExpireTime()))
                .refreshToken(requestDto.getRefreshToken())
                .refreshTokenExpireTime(formatter.parse(requestDto.getRefreshTokenExpireTime()))
                .build();
    }

    public boolean isExistNickname(String nickname) {
        return memberRepository.findByNickname(nickname).isPresent();
    }

    public MemberDto findDtoById(long memberId) {
        return memberConverter.convertToDto(memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND)));
    }

    public Member findById(long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
    }

}