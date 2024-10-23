package dmu.cheek.member.model;

import dmu.cheek.collection.model.Collection;
import dmu.cheek.global.token.dto.JwtTokenDto;
import dmu.cheek.global.util.DateTimeUtils;
import dmu.cheek.member.constant.MemberType;
import dmu.cheek.member.constant.Role;
import dmu.cheek.member.constant.Status;
import dmu.cheek.memberConnection.model.MemberConnection;
import dmu.cheek.global.auditing.BaseTimeEntity;
import dmu.cheek.highlight.model.Highlight;
import dmu.cheek.noti.model.Notification;
import dmu.cheek.question.model.Question;
import dmu.cheek.story.model.Story;
import dmu.cheek.upvote.model.Upvote;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long memberId;

    @Column(unique = true, nullable = false, length = 20)
    private String nickname;

    @Column(unique = true, nullable = false, length = 50)
    private String email;

    private String information;

    private String description;

    private String profilePicture;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10 )
    private MemberType memberType;

    @Enumerated(value = EnumType.STRING)
    private Role role;

    @Enumerated(value = EnumType.STRING)
    private Status status;

    private String firebaseToken;

    private String refreshToken;

    private LocalDateTime tokenExpirationTime;

    @OneToMany(mappedBy = "toMember", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Notification> toNotificationList = new ArrayList<>();

    @OneToMany(mappedBy = "fromMember", fetch = FetchType.LAZY)
    private List<Notification> fromNotificationList = new ArrayList<>();

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Question> questionList = new ArrayList<>();

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Story> storyList = new ArrayList<>();

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Highlight> highlightList = new ArrayList<>();

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Upvote> upvoteList = new ArrayList<>();

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Collection> collectionList = new ArrayList<>();

    @OneToMany(mappedBy = "toMember", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<MemberConnection> toMemberConnectionList = new ArrayList<>();

    @OneToMany(mappedBy = "fromMember", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<MemberConnection> fromMemberConnectionList = new ArrayList<>();


    @Builder(builderMethodName = "withEmail") //TODO: delete
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

    @Builder(builderMethodName = "joinBuilder")
    public Member(MemberType memberType, String email, Role role) {
        this.memberType = memberType;
        this.email = email;
        this.role = role;
    }

    public void setProfile(String nickname, String information, Role role, Status status) {
        this.nickname = nickname;
        this.information = information;
        this.role = role;
        this.status = status;
    }

    public void update(String nickname, String information, String description) {
        this.nickname = nickname;
        this.information = information;
        this.description = description;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public void updateRole(Role role) {
        this.role = role;
    }

    public void setFirebaseToken(String firebaseToken) {
        this.firebaseToken = firebaseToken;
    }

    public void updateRefreshToken(JwtTokenDto jwtTokenDto) {
        this.refreshToken = jwtTokenDto.getRefreshToken();
        this.tokenExpirationTime = DateTimeUtils.convertToLocalDateTime(jwtTokenDto.getRefreshTokenExpireTime());
    }

    public void expireRefreshToken(LocalDateTime now) {
        this.tokenExpirationTime = now;
    }
}
