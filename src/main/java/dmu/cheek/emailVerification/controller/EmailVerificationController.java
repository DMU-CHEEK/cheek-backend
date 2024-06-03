package dmu.cheek.emailVerification.controller;

import dmu.cheek.emailVerification.model.EmailVerificationDto;
import dmu.cheek.emailVerification.service.EmailVerificationService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/email")
@Slf4j
public class EmailVerificationController {

    private final EmailVerificationService emailVerificationService;

    @PostMapping("/send")
    public ResponseEntity<String> sendAuthCode(@RequestBody EmailVerificationDto emailVerificationRequestDto) throws MessagingException {
        emailVerificationService.registerEmailVerification(emailVerificationRequestDto);

        return ResponseEntity.ok("ok");
    }

    @PostMapping("/verify-code")
    public ResponseEntity<String> verifyEmailCode(@RequestBody EmailVerificationDto emailVerificationDto) {
        emailVerificationService.verifyCode(emailVerificationDto);

        return ResponseEntity.ok("ok");
    }

}
