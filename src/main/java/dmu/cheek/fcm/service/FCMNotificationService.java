package dmu.cheek.fcm.service;

import com.google.firebase.messaging.FirebaseMessaging;
import dmu.cheek.fcm.model.FCMNotificationRequestDto;
import dmu.cheek.member.model.Member;
import dmu.cheek.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FCMNotificationService {

    private final FirebaseMessaging firebaseMessaging;
    private final MemberService memberService;

    public void sendNotificationByToken(FCMNotificationRequestDto fcmNotificationRequestDto) {

        Member member = memberService.findById(fcmNotificationRequestDto.getMemberId());
        //TODO
    }
}
