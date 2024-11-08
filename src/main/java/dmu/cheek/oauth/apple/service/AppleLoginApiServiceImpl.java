package dmu.cheek.oauth.apple.service;

import dmu.cheek.global.error.exception.BusinessException;
import dmu.cheek.member.constant.MemberType;
import dmu.cheek.member.model.Member;
import dmu.cheek.member.service.MemberService;
import dmu.cheek.oauth.apple.client.AppleTokenClient;
import dmu.cheek.oauth.apple.model.ApplePublicKeys;
import dmu.cheek.oauth.apple.util.ApplePublicKeyGenerator;
import dmu.cheek.oauth.apple.util.AppleTokenParser;
import dmu.cheek.oauth.model.OAuthAttributes;
import dmu.cheek.oauth.service.SocialLoginApiService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.PublicKey;
import java.util.Map;

@Slf4j
@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class AppleLoginApiServiceImpl implements SocialLoginApiService {

    private final AppleTokenParser appleTokenParser;
    private final AppleTokenClient appleTokenClient;
    private final ApplePublicKeyGenerator applePublicKeyGenerator;
    private final MemberService memberService;

    private final String CLAIM_EMAIL = "email";

    @Override
    public OAuthAttributes getUserInfo(String accessToken) {
        final Map appleTokenHeader = appleTokenParser.parseHeader(accessToken);
        final ApplePublicKeys applePublicKeys = appleTokenClient.getApplePublicKeys();
        final PublicKey publicKey = applePublicKeyGenerator.generate(appleTokenHeader, applePublicKeys);
        final Claims claims = appleTokenParser.extractClaims(accessToken, publicKey);

        return OAuthAttributes.builder()
                .email(claims.get(CLAIM_EMAIL, String.class))
                .memberType(MemberType.APPLE)
                .build();
    }
}
