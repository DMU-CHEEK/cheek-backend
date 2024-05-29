package dmu.cheek.member.controller;

import dmu.cheek.kakao.controller.KakaoLoginClient;
import dmu.cheek.kakao.model.KakaoLoginDto;
import dmu.cheek.kakao.model.KakaoLoginResponseDto;
import dmu.cheek.member.converter.MemberConverter;
import dmu.cheek.member.model.Member;
import dmu.cheek.member.model.MemberDto;
import dmu.cheek.member.model.ProfileDto;
import dmu.cheek.member.service.MemberService;
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
public class MemberController {

    private final MemberService memberService;
    private final KakaoLoginClient kakaoLoginClient;
    private final MemberConverter memberConverter;

    @PostMapping("/login")
    public ResponseEntity<String> loginController(@RequestBody KakaoLoginDto.Request requestDto,
                                                  HttpServletRequest httpServletResponse) throws ParseException {
        String authorization = httpServletResponse.getHeader("Authorization");
        log.info("Authorization: {}", authorization);
        if (!StringUtils.hasText(authorization))
            throw new RuntimeException("authorization is null"); //TODO: exception

        String[] splitHeader = authorization.split(" ");
        if (splitHeader.length < 2)
            throw new RuntimeException("splitHeader.length < 2"); //TODO: exception
        if (!"Bearer".equals(splitHeader[0]))
            throw new RuntimeException("! \"Bearer\".equals(splitHeader[0]");

        String contentType = "application/x-www-form-urlencoded/charset=utf-8";
        KakaoLoginResponseDto kakaoLoginResponseDto = kakaoLoginClient.getKakaoUserInfo(contentType, authorization);

        if (!memberService.isExistMember(kakaoLoginResponseDto.getKakaoAccount().getEmail())) {
            log.info("member does not exists");
            memberService.register(kakaoLoginResponseDto);
            log.info("register successful");
        } else {
            log.info("member already exists");
            memberService.login(requestDto, kakaoLoginResponseDto);
            log.info("login successful");
        }

        return ResponseEntity.ok("ok");
    }

    @PostMapping(value = "/profile", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> setProfile(@RequestPart(value = "profileDto") ProfileDto profileDto,
                                             @RequestPart(value = "profilePicture") MultipartFile profilePicture) {

        memberService.setProfile(profileDto, profilePicture);

        return ResponseEntity.ok("ok");
    }

    @GetMapping("/info")
    public ResponseEntity<MemberDto> getMemberInfo(@RequestParam(value = "accessToken") String accessToken) {
        String contentType = "application/x-www-form-urlencoded/charset=utf-8";
        KakaoLoginResponseDto kakaoLoginResponseDto = kakaoLoginClient.getKakaoUserInfo(contentType, accessToken);
        MemberDto memberDto = memberConverter.convertToDto(memberService.findByEmail(kakaoLoginResponseDto.getKakaoAccount().getEmail()));

        log.info("get member info: {}", memberDto.getEmail());

        return ResponseEntity.ok(memberDto);
    }
}
