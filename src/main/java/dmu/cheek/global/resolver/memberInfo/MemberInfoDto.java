package dmu.cheek.global.resolver.memberInfo;

import dmu.cheek.member.constant.Role;
import lombok.Builder;
import lombok.Getter;

@Getter @Builder
public class MemberInfoDto {

    private long memberId;
    private Role role;
}
