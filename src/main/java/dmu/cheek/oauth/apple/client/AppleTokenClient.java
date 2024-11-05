package dmu.cheek.oauth.apple.client;

import dmu.cheek.oauth.apple.model.ApplePublicKeys;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "appleTokenClient", url = "https://appleid.apple.com")
public interface AppleTokenClient {

    @GetMapping("/auth/keys")
    ApplePublicKeys getApplePublicKeys(); //JWK 리스트 받아오기
}
