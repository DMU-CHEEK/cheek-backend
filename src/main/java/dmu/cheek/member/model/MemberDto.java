package dmu.cheek.member.model;

import dmu.cheek.member.constant.MemberType;
import dmu.cheek.member.constant.Role;
import dmu.cheek.member.constant.Status;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
public class MemberDto {

    private long memberId;

    @Email
    private String email;

    @Size(min = 4, max = 8, message = "nickname must be between 4 and 8 characters")
    @NotBlank(message = "nickname cannot be blank")
    private String nickname;

    @Size(max = 100, message = "description must be between 1 and 100 characters")
    private String description;

    @Size(max = 20, message = "information must be between 1 and 20 characters")
    @NotBlank(message = "information cannot be blank")
    private String information;

    private String profilePicture;

    private Role role;

    private Status status;

    @Builder @Getter @NoArgsConstructor @AllArgsConstructor
    public static class Top3MemberInfo {
        private long memberId;
        private String nickname;
        private String profilePicture;
        private String information;
        private String description;
    }

    @Builder @Getter @NoArgsConstructor @AllArgsConstructor
    public static class Concise {
        private long memberId;
        private String nickname;
        private String profilePicture;
    }

    @Getter @Builder
    public static class Info {
        private long memberId;
        private String email;
        private String nickname;
        private String description;
        private String information;
        private String profilePicture;
        private Role role;
        private Status status;
        private int followerCnt;
        private int followingCnt;
    }

    @Getter @Builder
    public static class List {
        private long memberId;
        private String nickname;
        private String email;
        private Role role;
        private MemberType memberType;
    }

}
