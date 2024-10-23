package dmu.cheek.oauth.kakao.dto;

import lombok.Getter;

@Getter
public class KakaoTokenInfoDto {

    private long id;
    private int expires_in;
    private int app_id;
}
