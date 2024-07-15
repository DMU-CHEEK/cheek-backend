package dmu.cheek.story.model;

import dmu.cheek.highlight.model.Highlight;
import dmu.cheek.member.model.Member;
import dmu.cheek.question.model.Category;
import dmu.cheek.question.model.Question;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@NoArgsConstructor
public class Story {

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
