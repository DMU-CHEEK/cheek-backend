package dmu.cheek.member.controller;

import dmu.cheek.member.constant.MemberType;
import dmu.cheek.member.model.OauthLoginDto;
import dmu.cheek.member.service.OauthLoginService;
import dmu.cheek.member.validator.OauthValidator;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/oauth")
public class OauthLoginController {

    private final OauthValidator oauthValidator;
    private final OauthLoginService oauthLoginService;

    @PostMapping("/login")
    public ResponseEntity<OauthLoginDto.Response> oauthLogin(@RequestBody OauthLoginDto.Request oauthLoginRequestDto,
                                                             HttpServletRequest httpServletRequest) {

        String authorization = httpServletRequest.getHeader("Authorization");
        oauthValidator.validateAuthorization(authorization);
        oauthValidator.validateMemberType(oauthLoginRequestDto.getMemberType());

        String accessToken = authorization.split(" ")[1];
        OauthLoginDto.Response oauthLoginResponseDto = oauthLoginService.oauthLogin(
                accessToken, MemberType.from(oauthLoginRequestDto.getMemberType()));

        return ResponseEntity.ok(oauthLoginResponseDto);
    }
}
