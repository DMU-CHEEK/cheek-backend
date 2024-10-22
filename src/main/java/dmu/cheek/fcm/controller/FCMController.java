package dmu.cheek.fcm.controller;


import dmu.cheek.fcm.model.FcmDto;
import dmu.cheek.fcm.service.FcmService;
import dmu.cheek.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/noti")
@RequiredArgsConstructor
@Tag(name = "Notification API", description = "알림 기능")
public class FCMController {

    private final FcmService fcmService;
    private final MemberService memberService;

    @PostMapping("/token")
    @Operation(summary = "토큰 등록", description = "Firebase 토큰 등록 API")
    public ResponseEntity<String> registerToken(@RequestBody FcmDto.Token fcmDto) {
        fcmService.registerToken(fcmDto);

        return ResponseEntity.ok("ok");
    }
}
