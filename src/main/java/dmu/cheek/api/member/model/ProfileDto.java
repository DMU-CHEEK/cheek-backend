package dmu.cheek.api.member.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProfileDto {

    private String email;

    private String nickname;

    private String information;

    private Role role;
}

