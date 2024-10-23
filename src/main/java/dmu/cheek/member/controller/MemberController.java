package dmu.cheek.member.controller;

import dmu.cheek.global.util.AuthorizationHeaderUtils;
import dmu.cheek.oauth.kakao.client.KakaoLoginClient;
import dmu.cheek.oauth.kakao.dto.KakaoLoginResponseDto;
import dmu.cheek.member.converter.MemberConverter;
import dmu.cheek.member.model.MemberDto;
import dmu.cheek.member.model.ProfileDto;
import dmu.cheek.member.constant.Role;
import dmu.cheek.member.service.MemberService;
import dmu.cheek.s3.service.S3Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    private final MemberConverter memberConverter;
    private final S3Service s3Service;

//    @PostMapping("/login")
//    @Operation(summary = "로그인", description = "로그인(회원가입) API")
//    public ResponseEntity<Boolean> loginController(@RequestBody KakaoLoginDto.Request requestDto,
//                                                  HttpServletRequest httpServletResponse) throws ParseException {
//        String authorization = httpServletResponse.getHeader("Authorization");
//        log.info("Authorization: {}", authorization);
//
//        String contentType = "application/x-www-form-urlencoded/charset=utf-8";
//        KakaoLoginResponseDto kakaoLoginResponseDto = kakaoLoginClient.getKakaoUserInfo(contentType, authorization);
//
//        boolean isNewMember = memberService.isExistMember(kakaoLoginResponseDto.getKakaoAccount().getEmail());
//
//        if (!isNewMember) {
//            log.info("member does not exist, registering new member");
//            memberService.register(kakaoLoginResponseDto);
//        } else {
//            log.info("member already exists, logging in");
//            KakaoLoginDto.Response loginResponse = memberService.login(requestDto, kakaoLoginResponseDto);
//
//            if (!ObjectUtils.isEmpty(loginResponse.getNickname()) && !ObjectUtils.isEmpty(loginResponse.getInformation()))
//                log.info("profile is complete");
//            else
//                log.info("profile is not complete");
//        }
//
//        // TODO: token 발급 로직
//
//        return ResponseEntity.ok(isNewMember);
//    }

    @PostMapping(value = "/profile", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "프로필 설정", description = "회원 등록 시 프로필 설정 API")
    public ResponseEntity<String> setProfile(@RequestPart(value = "profileDto") ProfileDto profileDto,
                                             @RequestPart(value = "profilePicture", required = false) MultipartFile profilePicture) {

        memberService.setProfile(profileDto, profilePicture);

        return ResponseEntity.ok("ok");
    }

    @PatchMapping(value = "/profile", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "프로필 수정", description = "프로필 수정 API")
    public ResponseEntity<String> updateProfile(@RequestPart(value = "profileDto") ProfileDto.Update profileDto,
                                                @RequestPart(value = "profilePicture", required = false) MultipartFile profilePicture) {
        memberService.updateProfile(profileDto, profilePicture);

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
                                                @RequestParam(name = "loginMemberId") long loginMemberId) {
        ProfileDto.Profile profile = memberService.getProfile(targetMemberId, loginMemberId);

        return ResponseEntity.ok(profile);
    }

    @GetMapping("/top-members")
    @Operation(summary = "상위 회원 3명 조회", description = "주간 좋아요 상위 회원 3명 조회 API")
    public ResponseEntity<List> getTop3MembersWithMostUpvotesInWeek() {
        List<MemberDto.Top3MemberInfo> top3MembersWithMostUpvotesInWeek = memberService.getTop3MembersWithMostUpvotesInWeek();

        return ResponseEntity.ok(top3MembersWithMostUpvotesInWeek);
    }

    @PostMapping("/role/{memberId}")
    @Operation(summary = "상태(역할) 변경", description = "상태(역할) 변경 API")
    public ResponseEntity<String> updateRole(@PathVariable(name = "memberId") long memberId,
                                             @RequestParam(name = "role") String role) {
        memberService.checkRole(memberId, Role.valueOf(role)); //TODO: @PreAuthorize 적용
        memberService.updateRole(memberId, role);

        return ResponseEntity.ok("ok");
    }

    @PostMapping("/role/check/{memberId}")
    @Operation(summary = "역할 검증", description = "역할 검증 API(임시)")
    public ResponseEntity<Boolean> checkRole(@PathVariable(name = "memberId") long memberId,
                                             @RequestParam(name = "role") String role) {
        boolean result = memberService.checkRole(memberId, Role.valueOf(role));

        return ResponseEntity.ok(result);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest httpServletRequest) {
        String authorization = httpServletRequest.getHeader("Authorization");
        AuthorizationHeaderUtils.validateAuthorization(authorization);

        String accessToken = authorization.split(" ")[1];

        memberService.logout(accessToken);

        return ResponseEntity.ok("ok");
    }
}