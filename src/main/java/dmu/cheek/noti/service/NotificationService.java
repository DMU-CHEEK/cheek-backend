package dmu.cheek.noti.service;

import dmu.cheek.fcm.service.FcmService;
import dmu.cheek.global.resolver.memberInfo.MemberInfoDto;
import dmu.cheek.noti.model.Notification;
import dmu.cheek.noti.model.NotificationDto;
import dmu.cheek.noti.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

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

    public List<NotificationDto> getList(MemberInfoDto memberInfoDto) {
        List<NotificationDto> notificationList = notificationRepository.findByToMemberId(memberInfoDto.getMemberId())
                .stream()
                .map(notification -> NotificationDto.builder()
                        .notificationId(notification.getNotificationId())
                        .type(notification.getType())
                        .typeId(notification.getTypeId())
                        .body(notification.getBody())
                        .picture(notification.getPicture())
                        .build()
                ).sorted(Comparator.comparing(NotificationDto::getNotificationId).reversed())
                .toList();

        log.info("get notification list, memberId: {}", memberInfoDto.getMemberId());

        return notificationList;
    }

    @Transactional
    public void deleteOne(long notificationId) {
        notificationRepository.deleteById(notificationId);

        log.info("delete single notification: {}", notificationId);
    }

    @Transactional
    public void deleteAll(MemberInfoDto memberInfoDto) {
        notificationRepository.deleteAllByToMemberId(memberInfoDto.getMemberId());

        log.info("delete batch notification: {}", memberInfoDto.getMemberId());
    }
}
