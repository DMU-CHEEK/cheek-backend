package dmu.cheek.member.controller;

import dmu.cheek.kakao.controller.KakaoLoginClient;
import dmu.cheek.kakao.model.KakaoLoginDto;
import dmu.cheek.kakao.model.KakaoLoginResponseDto;
import dmu.cheek.member.converter.MemberConverter;
import dmu.cheek.member.model.Member;
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
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Member API", description = "회원 기능")
public class MemberController {

    private final MemberService memberService;
    private final KakaoLoginClient kakaoLoginClient;
    private final MemberConverter memberConverter;

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "로그인(회원가입) API")
    public ResponseEntity<Boolean> loginController(@RequestBody KakaoLoginDto.Request requestDto,
                                                  HttpServletRequest httpServletResponse) throws ParseException {
        String authorization = httpServletResponse.getHeader("Authorization");
        log.info("Authorization: {}", authorization);

        String contentType = "application/x-www-form-urlencoded/charset=utf-8";
        KakaoLoginResponseDto kakaoLoginResponseDto = kakaoLoginClient.getKakaoUserInfo(contentType, authorization);

        boolean isNewMember = memberService.isExistMember(kakaoLoginResponseDto.getKakaoAccount().getEmail());

        if (!isNewMember) {
            log.info("member does not exist, registering new member");
            memberService.register(kakaoLoginResponseDto);
        } else {
            log.info("member already exists, logging in");
            KakaoLoginDto.Response loginResponse = memberService.login(requestDto, kakaoLoginResponseDto);

            if (!StringUtils.isEmpty(loginResponse.getNickname()) && !StringUtils.isEmpty(loginResponse.getInformation())) {
                log.info("profile is complete");
            }
        }

        // TODO: token 발급 로직

        //프로필이 완전하지 않은 경우
        log.info("profile is not complete");
        return ResponseEntity.ok(isNewMember);
    }

    @PostMapping(value = "/profile", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "프로필 설정", description = "회원 등록 시 프로필 설정 API")
    public ResponseEntity<String> setProfile(@RequestPart(value = "profileDto") ProfileDto profileDto,
                                             @RequestPart(value = "profilePicture", required = false) MultipartFile profilePicture) {

        memberService.setProfile(profileDto, profilePicture);

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
    public ResponseEntity<MemberDto> getMemberInfo(@RequestParam(value = "accessToken") String accessToken) {
        String contentType = "application/x-www-form-urlencoded/charset=utf-8";
        KakaoLoginResponseDto kakaoLoginResponseDto = kakaoLoginClient.getKakaoUserInfo(contentType, accessToken);
        MemberDto memberDto = memberConverter.convertToDto(memberService.findByEmail(kakaoLoginResponseDto.getKakaoAccount().getEmail()));

        return ResponseEntity.ok(memberDto);
    }

}
