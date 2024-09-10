package dmu.cheek.upvote.model;

import dmu.cheek.global.auditing.BaseTimeEntity;
import dmu.cheek.member.model.Member;
import dmu.cheek.story.model.Story;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Upvote extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long upvoteId;

    private boolean isUpvoted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "story_id")
    private Story story;

    @Builder(builderMethodName = "allFields")
    public Upvote(long upvoteId, boolean isUpvoted, Member member, Story story) {
        this.upvoteId = upvoteId;
        this.isUpvoted = isUpvoted;
        this.member = member;
        this.story = story;
    }

    @Builder(builderMethodName = "withoutPrimaryKey")
    public Upvote(Member member, Story story, boolean isUpvoted) {
        this.member = member;
        this.story = story;
        this.isUpvoted = isUpvoted;
    }

    @Builder(builderMethodName = "naturalFields")
    public Upvote(long upvoteId, boolean isUpvoted) {
        this.upvoteId = upvoteId;
        this.isUpvoted = isUpvoted;
    }

    public void toggleUpvote(boolean status) {
        this.isUpvoted = status;
    }
}
