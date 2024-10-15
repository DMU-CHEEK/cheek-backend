package dmu.cheek.fcm.service;

import com.google.firebase.messaging.FirebaseMessaging;
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

    public void sendNotificationByToken(FcmDto fcmDto) {

        Member member = memberService.findById(fcmDto.getMemberId());
        //TODO
    }

    @Transactional
    public void registerToken(FcmDto.Token fcmDto) {
        Member member = memberService.findById(fcmDto.getMemberId());
        member.setFirebaseToken(fcmDto.getFirebaseToken());

        log.info("set firebase token, memberId: {}", member.getMemberId());
    }
}
