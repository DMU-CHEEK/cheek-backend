package dmu.cheek.oauth.service;

import dmu.cheek.member.constant.MemberType;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class SocialLoginApiServiceFactory {

    private static Map<String, SocialLoginApiService> socialLoginApiServices;

    public SocialLoginApiServiceFactory(Map<String, SocialLoginApiService> socialLoginApiServices) {
        this.socialLoginApiServices = socialLoginApiServices;
    }

    public static SocialLoginApiService getSocialLoginApiService(MemberType memberType) {
        String socialLoginApiServiceBeanName = "";

        if (MemberType.KAKAO.equals(memberType))
            socialLoginApiServiceBeanName = "kakaoLoginApiServiceImpl";
        else if (MemberType.APPLE.equals(memberType))
            socialLoginApiServiceBeanName = "appleLoginApiServiceImpl";

        return socialLoginApiServices.get(socialLoginApiServiceBeanName);
    }
}
