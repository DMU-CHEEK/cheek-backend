package dmu.cheek.member.controller;

import dmu.cheek.global.util.AuthorizationHeaderUtils;
import dmu.cheek.member.constant.MemberType;
import dmu.cheek.member.model.OauthLoginDto;
import dmu.cheek.member.service.OauthLoginService;
import dmu.cheek.member.validator.OauthValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/oauth")
@Tag(name = "Oauth API")
public class OauthLoginController {

    private final OauthValidator oauthValidator;
    private final OauthLoginService oauthLoginService;

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "로그인 API")
    public ResponseEntity<OauthLoginDto.Response> oauthLogin(@RequestBody OauthLoginDto.Request oauthLoginRequestDto,
                                                             HttpServletRequest httpServletRequest) {

        String authorization = httpServletRequest.getHeader("Authorization");
        AuthorizationHeaderUtils.validateAuthorization(authorization);
        oauthValidator.validateMemberType(oauthLoginRequestDto.getMemberType());

        String accessToken = authorization.split(" ")[1];
        OauthLoginDto.Response oauthLoginResponseDto = oauthLoginService.oauthLogin(
                accessToken, MemberType.from(oauthLoginRequestDto.getMemberType()));

        return ResponseEntity.ok(oauthLoginResponseDto);
    }
}
