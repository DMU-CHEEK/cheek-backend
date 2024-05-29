package dmu.cheek.member.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberDto {

    private long memberId;

    private String email;

    private String nickname;

    private String description;

    private String information;

    private String profilePicture;

    private Role role;

    private Status status;
}
