package dmu.cheek.fcm.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class FCMNotificationRequestDto {
    private long memberId;
    private String title;
    private String body;
}
