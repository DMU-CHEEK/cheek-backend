package dmu.cheek.member.controller;

import dmu.cheek.global.resolver.memberInfo.MemberInfo;
import dmu.cheek.global.resolver.memberInfo.MemberInfoDto;
import dmu.cheek.global.util.AuthorizationHeaderUtils;
import dmu.cheek.oauth.kakao.client.KakaoLoginClient;
import dmu.cheek.oauth.kakao.dto.KakaoLoginResponseDto;
import dmu.cheek.member.model.MemberDto;
import dmu.cheek.member.model.ProfileDto;
import dmu.cheek.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Member API", description = "회원 기능")
public class MemberController {

    private final MemberService memberService;
    private final KakaoLoginClient kakaoLoginClient;

    @PostMapping(value = "/profile", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "프로필 설정", description = "회원 등록 시 프로필 설정 API")
    public ResponseEntity<String> setProfile(@RequestPart(value = "profileDto") ProfileDto.Register profileDto,
                                             @RequestPart(value = "profilePicture", required = false) MultipartFile profilePicture,
                                             @MemberInfo MemberInfoDto memberInfoDto) {

        memberService.setProfile(profileDto, profilePicture, memberInfoDto.getMemberId());

        return ResponseEntity.ok("ok");
    }

    @PatchMapping(value = "/profile", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "프로필 수정", description = "프로필 수정 API")
    public ResponseEntity<String> updateProfile(@RequestPart(value = "profileDto") ProfileDto.Update profileDto,
                                                @RequestPart(value = "profilePicture", required = false) MultipartFile profilePicture,
                                                @MemberInfo MemberInfoDto memberInfoDto) {
        memberService.updateProfile(profileDto, profilePicture, memberInfoDto);

        return ResponseEntity.ok("ok");
    }

    @GetMapping("check-nickname")
    @Operation(summary = "닉네임 유효성 검증", description = "닉네임 유효성 검증 API")
    public ResponseEntity<Boolean> checkNicknameValidity(@RequestParam(name = "nickname") String nickname) {
        boolean result = !memberService.isExistNickname(nickname);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/info")
    @Operation(summary = "회원정보 조회", description = "회원정보 조회 API")
    public ResponseEntity<MemberDto.Info> getMemberInfo(@RequestHeader(name = "Authorization") String accessToken) {
        String contentType = "application/x-www-form-urlencoded/charset=utf-8";
        KakaoLoginResponseDto kakaoLoginResponseDto = kakaoLoginClient.getKakaoUserInfo(contentType, accessToken);

        MemberDto.Info memberDto = memberService.getMemberAllInfo(kakaoLoginResponseDto.getKakaoAccount().getEmail());

        return ResponseEntity.ok(memberDto);
    }

    @GetMapping("/info/{targetMemberId}")
    @Operation(summary = "프로필 조회", description = "회원 프로필 조회 API")
    public ResponseEntity<ProfileDto.Profile> getProfile(@PathVariable(name = "targetMemberId") long targetMemberId,
                                                         @MemberInfo MemberInfoDto memberInfoDto) {
        ProfileDto.Profile profile = memberService.getProfile(targetMemberId, memberInfoDto);

        return ResponseEntity.ok(profile);
    }

    @GetMapping("/top-members")
    @Operation(summary = "상위 회원 3명 조회", description = "주간 좋아요 상위 회원 3명 조회 API")
    public ResponseEntity<List> getTop3MembersWithMostUpvotesInWeek() {
        List<MemberDto.Top3MemberInfo> top3MembersWithMostUpvotesInWeek = memberService.getTop3MembersWithMostUpvotesInWeek();

        return ResponseEntity.ok(top3MembersWithMostUpvotesInWeek);
    }

    @PostMapping("/role")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "상태(역할) 변경", description = "상태(역할) 변경 API")
    public ResponseEntity<String> updateRole(@RequestParam(name = "memberId") long memberId,
                                             @RequestParam(name = "role") String role) {
        memberService.updateRole(memberId, role);

        return ResponseEntity.ok("ok");
    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃", description = "로그아웃 API")
    public ResponseEntity<String> logout(HttpServletRequest httpServletRequest) {
        String authorization = httpServletRequest.getHeader("Authorization");
        AuthorizationHeaderUtils.validateAuthorization(authorization);

        String accessToken = authorization.split(" ")[1];

        memberService.logout(accessToken);

        return ResponseEntity.ok("ok");
    }

    @GetMapping("/info/all")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "전체 회원 목록 조회", description = "전체 회원 목록 조회 API")
    public ResponseEntity<List> getMemberList() {
        List<MemberDto.List> memberList = memberService.getMemberList();

        return ResponseEntity.ok(memberList);
    }
}