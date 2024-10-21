package dmu.cheek.fcm.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import dmu.cheek.fcm.model.FcmDto;
import dmu.cheek.member.model.Member;
import dmu.cheek.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class FcmService {

    private final FirebaseMessaging firebaseMessaging;
    private final MemberService memberService;

    public void sendNotificationByToken(FcmDto fcmDto) throws FirebaseMessagingException {
        Member member = memberService.findById(fcmDto.getMemberId());

        String message = FirebaseMessaging.getInstance().send(
                Message.builder()
                        .setNotification(Notification.builder()
                                .setTitle(fcmDto.getTitle())
                                .setBody(fcmDto.getBody())
                                .build()
                        )
                        .setToken(fcmDto.getFirebaseToken())
                        .build()
        );

        log.info("send notification: {}", message);

    }

    @Transactional
    public void registerToken(FcmDto.Token fcmDto) {
        Member member = memberService.findById(fcmDto.getMemberId());
        member.setFirebaseToken(fcmDto.getFirebaseToken());

        log.info("set firebase token, memberId: {}", member.getMemberId());
    }
}
