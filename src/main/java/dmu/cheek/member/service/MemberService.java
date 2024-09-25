package dmu.cheek.member.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final S3Service s3Service;
    private final MemberConverter memberConverter;
    private final RedisTemplate<String, Object> redisTemplate;

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

    public List<MemberDto.Top3MemberInfo> getTop3MembersWithMostUpvotesInWeek() {
        List<Object> topMembers = redisTemplate.opsForList().range("topMembers", 0, -1);
        ObjectMapper objectMapper = new ObjectMapper();
        List<MemberDto.Top3MemberInfo> memberInfoList = new ArrayList<>();

        for (Object topMember : topMembers) {
            try {
                String stringValue = objectMapper.writeValueAsString(topMember);

                MemberDto.Top3MemberInfo[] memberArray = objectMapper.readValue(stringValue, MemberDto.Top3MemberInfo[].class);
                memberInfoList.addAll(Arrays.asList(memberArray));
            } catch (Exception e) {
                log.error("Error converting Redis data to MemberDto.Top3MemberInfo", e);
            }
        }

        log.info("retrieving top 3 members by upvotes");

        return memberInfoList;
    }

    @Transactional
    public void updateRole(long memberId, String role) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        member.updateRole(Role.from(role));

        log.info("update member {} role: {} to {}", memberId, member.getRole(), role);
    }

    public boolean checkRole(long memberId, Role role) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
        return member.getRole() == role;
    }

}