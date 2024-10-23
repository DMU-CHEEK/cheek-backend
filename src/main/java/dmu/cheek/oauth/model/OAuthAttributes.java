package dmu.cheek.oauth.model;

import dmu.cheek.member.model.Member;
import dmu.cheek.member.constant.MemberType;
import dmu.cheek.member.constant.Role;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OAuthAttributes {

    private String email;

    private MemberType memberType;

    public Member toMember(MemberType memberType, Role role) {
        return Member.joinBuilder()
                .memberType(memberType)
                .email(email)
                .role(role)
                .build();
    }
}
