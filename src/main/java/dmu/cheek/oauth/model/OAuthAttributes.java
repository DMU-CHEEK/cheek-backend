package dmu.cheek.oauth.model;

import dmu.cheek.member.constant.MemberType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OAuthAttributes {

    private String email;

    private MemberType memberType;
}
