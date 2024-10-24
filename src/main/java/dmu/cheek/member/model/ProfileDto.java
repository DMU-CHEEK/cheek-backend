package dmu.cheek.member.model;

import dmu.cheek.member.constant.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
public class ProfileDto {

    @Email
    private String email;

    @Size(min = 4, max = 8, message = "nickname must be between 4 and 8 characters")
    @NotBlank(message = "nickname cannot be blank")
    private String nickname;

    @Size(max = 20, message = "information must be between 1 and 20 characters")
    @NotBlank(message = "information cannot be blank")
    private String information;

    private Role role;

    @Builder @Getter
    @NoArgsConstructor @AllArgsConstructor
    public static class Profile {
        private long memberId;
        private String nickname;
        private String information;
        private String description;
        private String profilePicture;
        private Role role;
        private boolean isFollowing;
        private int followerCnt;
        private int followingCnt;
    }

    @Getter
    public static class Update {
        private String nickname;
        private String information;
        private String description;
    }
}

