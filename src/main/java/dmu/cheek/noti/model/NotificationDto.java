package dmu.cheek.noti.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @Builder @NoArgsConstructor @AllArgsConstructor
public class NotificationDto {

    private long notificationId;
    private Type type;
    private long typeId;
    private String body;

    @Builder(builderMethodName = "withoutPrimaryKey")
    public NotificationDto(Type type, long typeId, String body) {
        this.body = body;
        this.type = type;
        this.typeId = typeId;
    }

    public static class Response {
        private long notificationId;
        private String body;
    }
}
