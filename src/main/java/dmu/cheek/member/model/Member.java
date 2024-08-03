package dmu.cheek.member.model;

import dmu.cheek.global.auditing.BaseTimeEntity;
import dmu.cheek.highlight.model.Highlight;
import dmu.cheek.question.model.Question;
import dmu.cheek.story.model.Story;
import dmu.cheek.upvote.model.Upvote;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Member extends BaseTimeEntity {

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

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Question> questionList = new ArrayList<>();

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Story> storyList = new ArrayList<>();

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Highlight> highlightList = new ArrayList<>();

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Upvote> upvoteList = new ArrayList<>();

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

    @Builder(builderMethodName = "allFields")
    public Member(long memberId, String nickname, String email, String information, String description, String profilePicture, Role role, Status status) {
        this.memberId = memberId;
        this.email = email;
        this.nickname = nickname;
        this.information = information;
        this.description = description;
        this.profilePicture = profilePicture;
        this.role = role;
        this.status = status;
    }


    public void setProfile(String nickname, String information, Role role, Status status) {
        this.nickname = nickname;
        this.information = information;
        this.role = role;
        this.status = status;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }
}
