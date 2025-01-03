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
    private String picture;

    @Builder(builderMethodName = "withoutPrimaryKey")
    public NotificationDto(Type type, long typeId, String body, String picture) {
        this.body = body;
        this.type = type;
        this.typeId = typeId;
        this.picture = picture;
    }
}
