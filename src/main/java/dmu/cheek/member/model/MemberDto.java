package dmu.cheek.member.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

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

}
