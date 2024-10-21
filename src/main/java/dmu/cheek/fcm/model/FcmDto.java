package dmu.cheek.fcm.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class FcmDto {
    private long memberId;
    private String firebaseToken;
    private String title;
    private String body;

    @Getter
    public static class Token {
        private long memberId;
        private String firebaseToken;
    }
}
