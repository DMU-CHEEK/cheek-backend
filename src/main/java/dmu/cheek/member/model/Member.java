package dmu.cheek.member.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private long memberId;

    private String nickname;

    private String email;

    private String information;

    private String description;

    @Column(name = "profile_picture")
    private String profilePicture;

    @Enumerated(value = EnumType.STRING)
    private Role role;

    @Enumerated(value = EnumType.STRING)
    private Status status;

    @Builder(builderMethodName = "withoutPrimaryKey")
    public Member(String nickname, String email, String information, String description, String profilePicture, Role role, Status status) {
        this.nickname = nickname;
        this.email = email;
        this.information = information;
        this.description = description;
        this.profilePicture = profilePicture;
        this.role = role;
        this.status = status;
    }

    @Builder(builderMethodName = "withEmail")
    public Member(String email) {
        this.email = email;
    }

    public void setProfile(String nickname, String information, String profilePicture, Role role) {
        this.nickname = nickname;
        this.information = information;
        this.profilePicture = profilePicture;
        this.role = role;
    }
}
