package dmu.cheek.member.validator;

import dmu.cheek.global.error.ErrorCode;
import dmu.cheek.global.error.exception.AuthenticationException;
import dmu.cheek.global.token.constant.GrantType;
import dmu.cheek.member.constant.MemberType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class OauthValidator {

    public void validateMemberType(String memberType) {
        if (MemberType.from(memberType) == MemberType.NONE)
            throw new AuthenticationException(ErrorCode.INVALID_MEMBER_TYPE);
    }
}
