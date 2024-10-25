package dmu.cheek.oauth.apple.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "appleLoginClient", url = "https://appleid.apple.com/auth")
public interface AppleLoginClient {
}
