package dmu.cheek.emailVerification.controller;

import dmu.cheek.emailVerification.model.EmailVerificationDto;
import dmu.cheek.emailVerification.service.EmailVerificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/email")
@Tag(name = "EmailVerification API", description = "이메일 검증 API")
@Slf4j
public class EmailVerificationController {

    private final EmailVerificationService emailVerificationService;

    @PostMapping("/register-domain")
    @Operation(summary = "도메인 추가 요청", description = "무효 도메인 추가 요청 API")
    public ResponseEntity<String> registerDomain(@RequestParam(name = "domain") String domain) {
        emailVerificationService.registerDomain(domain);

        return ResponseEntity.ok("ok");
    }

    @GetMapping("/verify-domain")
    @Operation(summary = "도메인 유효성 검증", description = "도메인 유효성 검증 API")
    public ResponseEntity<Boolean> verifyDomain(@RequestParam(name = "domain") String domain) {
        boolean validateDomain = emailVerificationService.validateDomain(domain);
        return ResponseEntity.ok(validateDomain);
    }

    @PostMapping("/send")
    @Operation(summary = "이메일 코드 전송", description = "이메일 인증코드 전송 API")
    public ResponseEntity<String> sendAuthCode(@RequestBody EmailVerificationDto emailVerificationRequestDto) throws MessagingException {
        emailVerificationService.registerEmailVerification(emailVerificationRequestDto);

        return ResponseEntity.ok("ok");
    }

    @PostMapping("/verify-code")
    @Operation(summary = "이메일 코드 검증", description = "이메일 인증코드 검증 API")
    public ResponseEntity<String> verifyEmailCode(@RequestBody EmailVerificationDto emailVerificationDto) {
        emailVerificationService.verifyCode(emailVerificationDto);

        return ResponseEntity.ok("ok");
    }

}
