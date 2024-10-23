package dmu.cheek.oauth.kakao.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;


public class KakaoTokenDto {

    @Getter @Builder
    public static class Request {
        private String grant_type;
        private String client_id;
        private String redirect_uri;
        private String code;
        private String client_secret;
    }

    @Getter @Builder
    public static class Response {
        private String token_type;
        private String access_token;
        private Integer expires_in;
        private String refresh_token;
        private Integer refresh_token_expires_in;
        private String scope;
    }

}