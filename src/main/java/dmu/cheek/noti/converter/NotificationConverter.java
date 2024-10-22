package dmu.cheek.noti.converter;

import dmu.cheek.noti.model.Notification;
import dmu.cheek.noti.model.NotificationDto;
import org.springframework.stereotype.Component;

@Component
public class NotificationConverter {

    public NotificationDto convertToDto(Notification notification) {
        if (notification == null)
            return null;

        return NotificationDto.builder()
                .notificationId(notification.getNotificationId())
                .type(notification.getType())
                .typeId(notification.getTypeId())
                .body(notification.getBody())
                .build();
    }

    public Notification convertToEntity(NotificationDto notificationDto) {
        if (notificationDto == null)
            return null;

        return Notification.naturalFields()
                .notificationId(notificationDto.getNotificationId())
                .body(notificationDto.getBody())
                .type(notificationDto.getType())
                .typeId(notificationDto.getTypeId())
                .build();
    }

}
