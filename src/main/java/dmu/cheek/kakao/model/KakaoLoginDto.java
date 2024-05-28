package dmu.cheek.kakao.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import dmu.cheek.member.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

public class KakaoLoginDto {

    @Getter @Builder
    @NoArgsConstructor @AllArgsConstructor
    public static class Request {
        private String accessToken;

        private String refreshToken;

        private String accessTokenExpireTime;

        private String refreshTokenExpireTime;
    }

    @Getter @Builder
    @NoArgsConstructor @AllArgsConstructor
    public static class Response {
        private long memberId;

        private String email;

        private String nickname;

        private String profile;

        private Role role;

        private String accessToken;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private Date accessTokenExpireTime;

        private String refreshToken;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private Date refreshTokenExpireTime;

    }
}
