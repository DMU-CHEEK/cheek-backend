package dmu.cheek.api.kakao.controller;

import dmu.cheek.api.kakao.model.KakaoLoginResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Map;

@FeignClient(url = "https://kapi.kakao.com", name = "kakaoLoginClient")
public interface KakaoLoginClient {

    @GetMapping(value = "/v2/user/me", consumes = "application/json")
    KakaoLoginResponseDto getkakaoUserInfo(@RequestHeader("Content-Type") String contentType,
                                           @RequestHeader("Authorization") String accessToken);

    @PostMapping(value = "/v1/user/logout", consumes = "application/json")
    Map<String, Object> logoutKakaoUser(@RequestHeader("Content-Type") String contentType,
                                        @RequestHeader("Authorization") String accessToken);
}
