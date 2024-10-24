package dmu.cheek.fcm.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import dmu.cheek.fcm.model.FcmDto;
import dmu.cheek.global.error.ErrorCode;
import dmu.cheek.global.error.exception.BusinessException;
import dmu.cheek.global.resolver.memberInfo.MemberInfoDto;
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

    public void sendNotificationByToken(dmu.cheek.noti.model.Notification notification) {
        if (notification.getToMember().getFirebaseToken() != null) {

            Message message = Message.builder()
                    .setNotification(Notification.builder()
                            .setBody(notification.getBody())
                            .build()
                    )
                    .setToken(notification.getToMember().getFirebaseToken())
                    .build();

            try {
                String response = firebaseMessaging.send(message);
                log.info("send notification, toMemberId: {}, response: {}", notification.getToMember().getMemberId(), response);
            } catch (FirebaseMessagingException e) {
                log.info("notification delivery failed: {}", e.toString());
                throw new BusinessException(ErrorCode.NOTIFICATION_SENDING_FAILED);
            }
        } else {
            throw new BusinessException(ErrorCode.TOKEN_NOT_FOUND);
        }
    }

    @Transactional
    public void registerToken(FcmDto.Token fcmDto, MemberInfoDto memberInfoDto) {
        Member member = memberRepository.findById(memberInfoDto.getMemberId())
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
        member.setFirebaseToken(fcmDto.getFirebaseToken());

        log.info("set firebase token, memberId: {}", member.getMemberId());
    }
}
