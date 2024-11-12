package dmu.cheek.member.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import dmu.cheek.global.error.ErrorCode;
import dmu.cheek.global.error.exception.AuthenticationException;
import dmu.cheek.global.error.exception.BusinessException;
import dmu.cheek.global.resolver.memberInfo.MemberInfoDto;
import dmu.cheek.global.token.constant.TokenType;
import dmu.cheek.global.token.service.TokenManager;
import dmu.cheek.member.constant.MemberType;
import dmu.cheek.member.constant.Role;
import dmu.cheek.member.constant.Status;
import dmu.cheek.oauth.kakao.dto.KakaoLoginDto;
import dmu.cheek.oauth.kakao.dto.KakaoLoginResponseDto;
import dmu.cheek.member.converter.MemberConverter;
import dmu.cheek.member.model.*;
import dmu.cheek.member.repository.MemberRepository;
import dmu.cheek.noti.model.Notification;
import dmu.cheek.noti.model.Type;
import dmu.cheek.noti.service.NotificationService;
import dmu.cheek.s3.model.S3Dto;
import dmu.cheek.s3.service.S3Service;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
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
    private final NotificationService notificationService;
    private final TokenManager tokenManager;

    public Optional<Member> findByEmailAndMemberType(String email, MemberType memberType) {
        return memberRepository.findByEmailAndMemberType(email, memberType);
    }

    @Transactional
    public Member register(Member member) {

        Member registerMember = memberRepository.save(member);

        log.info("save member: {}", member.getEmail());

        return registerMember;
    }

    @Transactional
    public void setProfile(ProfileDto.Register profileDto, MultipartFile profilePicture, long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        if (profilePicture != null) {
            S3Dto s3Dto = s3Service.saveFile(profilePicture);
            member.setProfilePicture(s3Dto.getStoreFileName());
        }

        member.setProfile(profileDto.getNickname(), profileDto.getInformation(), profileDto.getRole(), Status.COMPLETE);


        log.info("set profile: {}", member.getMemberId());
    }

    @Transactional
    public void updateProfile(ProfileDto.Update profileDto, MultipartFile profilePicture, MemberInfoDto memberInfoDto) {
        Member member = memberRepository.findById(memberInfoDto.getMemberId())
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        if (profilePicture != null) {
            S3Dto s3Dto = s3Service.saveFile(profilePicture);
            member.setProfilePicture(s3Dto.getStoreFileName());
        }

        member.update(profileDto.getNickname(), profileDto.getInformation(), profileDto.getDescription());
    }


    public MemberDto.Info getMemberAllInfo(String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow(
                () -> new BusinessException(ErrorCode.EMAIL_NOT_FOUND));

        log.info("get member {}'s all info", member.getMemberId());

        return MemberDto.Info.builder()
                .memberId(member.getMemberId())
                .email(member.getEmail())
                .information(member.getInformation())
                .description(member.getDescription())
                .nickname(member.getNickname())
                .profilePicture(s3Service.getResourceUrl(member.getProfilePicture()))
                .role(member.getRole())
                .status(member.getStatus())
                .followerCnt(member.getToMemberConnectionList().size())
                .followingCnt(member.getFromMemberConnectionList().size())
                .build();
    }

    public KakaoLoginDto.Response login(KakaoLoginDto.Request requestDto, KakaoLoginResponseDto kakaoLoginResponseDto) throws ParseException {
        String email = kakaoLoginResponseDto.getKakaoAccount().getEmail();
        Member member = memberRepository.findByEmailAndMemberType(email, MemberType.KAKAO).orElseThrow(
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

    public ProfileDto.Profile getProfile(long targetMemberId, MemberInfoDto memberInfoDto) {
        Member targetMember = memberRepository.findById(targetMemberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
        Member loginMember = memberRepository.findById(memberInfoDto.getMemberId())
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        boolean isFollowing = loginMember.getFromMemberConnectionList().stream()
                .anyMatch(connection -> connection.getToMember().getMemberId() == targetMemberId);

        return ProfileDto.Profile.builder()
                .memberId(targetMemberId)
                .nickname(targetMember.getNickname())
                .description(targetMember.getDescription())
                .information(targetMember.getInformation())
                .profilePicture(s3Service.getResourceUrl(targetMember.getProfilePicture()))
                .isFollowing(isFollowing)
                .followerCnt(targetMember.getToMemberConnectionList().size())
                .followingCnt(targetMember.getFromMemberConnectionList().size())
                .role(targetMember.getRole())
                .build();
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

        log.info("retrieving top 3 members by upvote count");

        return memberInfoList;
    }

    @Transactional
    public void updateRole(long memberId, String role) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        member.updateRole(Role.from(role));

        log.info("update member {} role: {} to {}", member.getMemberId(), member.getRole(), role);

        //send notification
        String notiBody = "멘토 회원 승인이 정상적으로 완료되었어요!";

        notificationService.register(
                Notification.withoutPrimaryKey()
                        .type(Type.from("ROLE"))
                        .typeId(member.getMemberId())
                        .toMember(member)
                        .body(notiBody)
                        .build()
        );
    }

    public Member findByRefreshToken(String refreshToken) {
        Member member = memberRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new AuthenticationException(ErrorCode.REFRESH_TOKEN_NOT_FOUND));

        LocalDateTime tokenExpirationTime = member.getTokenExpirationTime();

        if (tokenExpirationTime.isBefore(LocalDateTime.now()))
            throw new BusinessException(ErrorCode.REFRESH_TOKEN_EXPIRED);

        return member;
    }

    @Transactional
    public void logout(String accessToken) {
        tokenManager.validateToken(accessToken);

        Claims tokenClaims = tokenManager.getTokenClaims(accessToken);
        String tokenType = tokenClaims.getSubject();

        if (!TokenType.isAccessType(tokenType))
            throw new AuthenticationException(ErrorCode.NOT_ACCESS_TOKEN_TYPE);

        //refresh token 만료 처리
        long memberId = Long.valueOf((Integer) tokenClaims.get("memberId"));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        member.expireRefreshToken(LocalDateTime.now());
    }

    public List<MemberDto.List> getMemberList() {
        log.info("get member list");

        return memberRepository.findAll().stream()
                .map(member -> MemberDto.List.builder()
                        .memberId(member.getMemberId())
                        .nickname(member.getNickname())
                        .email(member.getEmail())
                        .role(member.getRole())
                        .memberType(member.getMemberType())
                        .build()
                )
                .toList();
    }

    @Transactional
    public void withdraw(long memberId) {
        memberRepository.deleteById(memberId);
    }
}