package dmu.cheek.oauth.apple.util;

import dmu.cheek.global.error.ErrorCode;
import dmu.cheek.global.error.exception.AuthenticationException;
import dmu.cheek.global.error.exception.BusinessException;
import dmu.cheek.oauth.apple.model.ApplePublicKey;
import dmu.cheek.oauth.apple.model.ApplePublicKeys;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.Map;

@Component
public class ApplePublicKeyGenerator {

    private static final String SIGN_ALGORITHM_HEADER = "alg";
    private static final String KEY_ID_HEADER = "kid";
    private static final int POSITIVE_SIGN_NUMBER = 1;

    public PublicKey generate(final Map<String, String> headers, final ApplePublicKeys publicKeys) {
        final ApplePublicKey applePublicKey = publicKeys.getMatchingKey(
                headers.get(SIGN_ALGORITHM_HEADER),
                headers.get(KEY_ID_HEADER)
        );
        return generatePublicKey(applePublicKey);
    }

    private PublicKey generatePublicKey(final ApplePublicKey applePublicKey) {
        final byte[] nBytes = Base64.getUrlDecoder().decode(applePublicKey.getN());
        final byte[] eBytes = Base64.getUrlDecoder().decode(applePublicKey.getE());

        final BigInteger n = new BigInteger(POSITIVE_SIGN_NUMBER, nBytes);
        final BigInteger e = new BigInteger(POSITIVE_SIGN_NUMBER, eBytes);
        final RSAPublicKeySpec rsaPublicKeySpec = new RSAPublicKeySpec(n, e);

        try {
            final KeyFactory keyFactory = KeyFactory.getInstance(applePublicKey.getKty());
            return keyFactory.generatePublic(rsaPublicKeySpec);
        } catch (NoSuchAlgorithmException e1) {
            throw new AuthenticationException(ErrorCode.INVALID_KEY_SPEC);
        } catch (InvalidKeySpecException e2) {
            throw new BusinessException(ErrorCode.ALGORITHM_NOT_FOUND);
        }
    }
}
