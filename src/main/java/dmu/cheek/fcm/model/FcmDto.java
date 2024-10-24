package dmu.cheek.fcm.model;

import dmu.cheek.member.model.MemberDto;
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
    private String body;

    @Getter
    public static class Token {
        private String firebaseToken;
    }
}
