package dmu.cheek.oauth.kakao.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import dmu.cheek.member.constant.Role;
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


    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private long memberId;

        private String email;

        private String nickname;

        private String profilePicture;

        private String information;

        private Role role;

        private String accessToken;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private Date accessTokenExpireTime;

        private String refreshToken;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private Date refreshTokenExpireTime;

        //TODO: delete
    }
}
