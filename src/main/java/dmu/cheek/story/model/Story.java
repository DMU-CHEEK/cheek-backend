package dmu.cheek.story.model;

import dmu.cheek.global.auditing.BaseTimeEntity;
import dmu.cheek.highlight.model.Highlight;
import dmu.cheek.member.model.Member;
import dmu.cheek.question.model.Category;
import dmu.cheek.question.model.Question;
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
public class Story extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "story_id")
    private long storyId;

    @Column(name = "story_picture")
    private String storyPicture;

    @JoinColumn(name = "member_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @JoinColumn(name = "category_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;

    @JoinColumn(name = "profession_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Question question;

    @JoinColumn(name = "highlight_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Highlight highlight;

    @OneToMany(mappedBy = "story", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Upvote> upvoteList = new ArrayList<>();

    @Builder
    public Story(String storyPicture, Member member, Category category, Question question) {
        this.storyPicture = storyPicture;
        this.member = member;
        this.category = category;
        this.question = question;
    }

    @Builder(builderMethodName = "naturalFields")
    public Story(long storyId, String storyPicture) {
        this.storyId = storyId;
        this.storyPicture = storyPicture;
    }

}
