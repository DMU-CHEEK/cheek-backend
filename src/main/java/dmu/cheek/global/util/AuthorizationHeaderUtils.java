package dmu.cheek.global.util;

import dmu.cheek.global.error.ErrorCode;
import dmu.cheek.global.error.exception.AuthenticationException;
import dmu.cheek.global.token.constant.GrantType;
import org.springframework.util.StringUtils;

public class AuthorizationHeaderUtils {

    public static void validateAuthorization(String authorization) {
        //1. authorization 필수 체크
        if (!StringUtils.hasText(authorization))
            throw new AuthenticationException(ErrorCode.NOT_EXIST_AUTHORIZATION);

        //2. prefix Bearer 체크
        String[] authorizations = authorization.split(" ");
        if (authorizations.length < 2 ||
                (!GrantType.BEARER.getGrantType().equals(authorizations[0])))
            throw new AuthenticationException(ErrorCode.NOT_VALID_BEARER_GRANT_TYPE);
    }
}
