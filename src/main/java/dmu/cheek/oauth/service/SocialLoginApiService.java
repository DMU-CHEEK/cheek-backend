package dmu.cheek.oauth.service;

import dmu.cheek.oauth.model.OAuthAttributes;

public interface SocialLoginApiService {

    OAuthAttributes getUserInfo(String accessToken);
}
