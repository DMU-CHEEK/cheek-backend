package dmu.cheek.fcm.controller;


import dmu.cheek.fcm.model.FcmDto;
import dmu.cheek.fcm.service.FcmService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/noti")
@RequiredArgsConstructor
public class FCMController {

    private final FcmService fcmService;

    @PostMapping("/token")
    public ResponseEntity<String> registerToken(@RequestBody FcmDto.Token fcmDto) {
        fcmService.registerToken(fcmDto);

        return ResponseEntity.ok("ok");
    }
}
