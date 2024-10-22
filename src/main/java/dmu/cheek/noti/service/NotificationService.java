package dmu.cheek.noti.service;

import com.google.firebase.messaging.FirebaseMessagingException;
import dmu.cheek.fcm.model.FcmDto;
import dmu.cheek.fcm.service.FcmService;
import dmu.cheek.noti.model.Notification;
import dmu.cheek.noti.model.NotificationDto;
import dmu.cheek.noti.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final FcmService fcmService;

    @Transactional
    public void register(Notification notification) {
        Notification registerNoti = notificationRepository.save(notification);

        log.info("register notification: {}", registerNoti.getNotificationId());

        fcmService.sendNotificationByToken(notification);
    }
}
