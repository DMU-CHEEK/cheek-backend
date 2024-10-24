package dmu.cheek.noti.controller;

import dmu.cheek.fcm.model.FcmDto;
import dmu.cheek.fcm.service.FcmService;
import dmu.cheek.global.resolver.memberInfo.MemberInfo;
import dmu.cheek.global.resolver.memberInfo.MemberInfoDto;
import dmu.cheek.noti.model.NotificationDto;
import dmu.cheek.noti.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/noti")
@RequiredArgsConstructor
@Tag(name = "Notification API", description = "알림 기능")
public class NotificationController {

    private final NotificationService notificationService;
    private final FcmService fcmService;

    @PostMapping("/token")
    @Operation(summary = "토큰 등록", description = "Firebase 토큰 등록 API")
    public ResponseEntity<String> registerToken(@RequestBody FcmDto.Token fcmDto,
                                                @MemberInfo MemberInfoDto memberInfoDto) {
        fcmService.registerToken(fcmDto, memberInfoDto);

        return ResponseEntity.ok("ok");
    }

    @GetMapping()
    @Operation(summary = "알림 목록 조회", description = "알림 목록 조회 API")
    public ResponseEntity<List> getList(@MemberInfo MemberInfoDto memberInfoDto) {
        List<NotificationDto> notificationDtoList = notificationService.getList(memberInfoDto);

        return ResponseEntity.ok(notificationDtoList);
    }

    @DeleteMapping("/{notificationId}")
    @Operation(summary = "알림 단건 삭제", description = "알림 단건 삭제 API")
    public ResponseEntity<String> deleteOne(@PathVariable(name = "notificationId") long notificationId) {
        notificationService.deleteOne(notificationId);

        return ResponseEntity.ok("ok");
    }

    @DeleteMapping()
    @Operation(summary = "알림 전체 삭제", description = "알림 전체 삭제 API")
    public ResponseEntity<String> deleteAll(@MemberInfo MemberInfoDto memberInfoDto) {
        notificationService.deleteAll(memberInfoDto);

        return ResponseEntity.ok("ok");
    }
}
