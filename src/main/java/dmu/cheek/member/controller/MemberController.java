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
    @Operation(summary = "로그인", description = "로그인/회원가입 API")
    public ResponseEntity<Boolean> loginController(@RequestBody KakaoLoginDto.Request requestDto,
                                                  HttpServletRequest httpServletResponse) throws ParseException {
        String authorization = httpServletResponse.getHeader("Authorization");
        log.info("Authorization: {}", authorization);

        if (!StringUtils.hasText(authorization) || !authorization.startsWith("Bearer "))
            throw new RuntimeException("invalid authorization header"); //TODO: exception

        String contentType = "application/x-www-form-urlencoded/charset=utf-8";
        KakaoLoginResponseDto kakaoLoginResponseDto = kakaoLoginClient.getKakaoUserInfo(contentType, authorization);

        KakaoLoginDto.Response loginResponse;

        if (!memberService.isExistMember(kakaoLoginResponseDto.getKakaoAccount().getEmail())) {
            log.info("member does not exist, registering new member");
            memberService.register(kakaoLoginResponseDto);
            return ResponseEntity.ok(false); //new member registered, profile not complete
        } else {
            log.info("member already exists, logging in");
            loginResponse = memberService.login(requestDto, kakaoLoginResponseDto);
        }

        boolean profileIncomplete = StringUtils.isEmpty(loginResponse.getNickname()) && StringUtils.isEmpty(loginResponse.getInformation());
        return ResponseEntity.ok(!profileIncomplete); //if profile is incomplete, return false
    }

    @PostMapping(value = "/profile", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "프로필 설정", description = "회원 등록 시 프로필 설정 API")
    public ResponseEntity<String> setProfile(@RequestPart(value = "profileDto") ProfileDto profileDto,
                                             @RequestPart(value = "profilePicture", required = false) MultipartFile profilePicture) {

        memberService.setProfile(profileDto, profilePicture);

        return ResponseEntity.ok("ok");
    }

    @GetMapping("/info")
    @Operation(summary = "회원정보 조회", description = "회원정보 조회 API")
    public ResponseEntity<MemberDto> getMemberInfo(@RequestParam(value = "accessToken") String accessToken) {
        String contentType = "application/x-www-form-urlencoded/charset=utf-8";
        KakaoLoginResponseDto kakaoLoginResponseDto = kakaoLoginClient.getKakaoUserInfo(contentType, accessToken);
        MemberDto memberDto = memberConverter.convertToDto(memberService.findByEmail(kakaoLoginResponseDto.getKakaoAccount().getEmail()));

        log.info("get member info: {}", memberDto.getEmail());

        return ResponseEntity.ok(memberDto);
    }

}
