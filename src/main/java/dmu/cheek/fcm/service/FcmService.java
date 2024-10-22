package dmu.cheek.fcm.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import dmu.cheek.fcm.model.FcmDto;
import dmu.cheek.global.error.ErrorCode;
import dmu.cheek.global.error.exception.BusinessException;
import dmu.cheek.member.model.Member;
import dmu.cheek.member.repository.MemberRepository;
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
    private final MemberRepository memberRepository;

    public void sendNotificationByToken(FcmDto fcmDto) throws FirebaseMessagingException {
        Member member = memberRepository.findById(fcmDto.getMemberId())
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        if (member.getFirebaseToken() != null) {

            Message message = Message.builder()
                    .setNotification(Notification.builder()
                            .setBody(fcmDto.getBody())
                            .build()
                    )
                    .setToken(fcmDto.getFirebaseToken())
                    .build();

            try {
                String response = firebaseMessaging.send(message);
                log.info("send notification, toMemberId: {}, response: {}", fcmDto.getMemberId(), response);
            } catch (FirebaseMessagingException e) {
                log.info("notification delivery failed: {}", e.toString());
            }
        } else {
            throw new BusinessException(ErrorCode.TOKEN_NOT_FOUND);
        }
    }

    @Transactional
    public void registerToken(FcmDto.Token fcmDto) {
        Member member = memberRepository.findById(fcmDto.getMemberId())
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
        member.setFirebaseToken(fcmDto.getFirebaseToken());

        log.info("set firebase token, memberId: {}", member.getMemberId());
    }
}
