package dmu.cheek.story.model;

import dmu.cheek.collection.model.Collection;
import dmu.cheek.global.auditing.BaseTimeEntity;
import dmu.cheek.highlight.model.HighlightStory;
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
    private long storyId;

    private String storyPicture;

    private String text;

    @JoinColumn(name = "member_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @JoinColumn(name = "category_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;

    @JoinColumn(name = "profession_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Question question;

    @OneToMany(mappedBy = "story", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<HighlightStory> highlightStoryList = new ArrayList<>();

    @OneToMany(mappedBy = "story", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Upvote> upvoteList = new ArrayList<>();

    @OneToMany(mappedBy = "story", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Collection> collectionList = new ArrayList<>();

    @Builder
    public Story(String storyPicture, String text, Member member, Category category, Question question) {
        this.storyPicture = storyPicture;
        this.text = text;
        this.member = member;
        this.category = category;
        this.question = question;
    }

    @Builder(builderMethodName = "naturalFields")
    public Story(long storyId, String storyPicture, String text) {
        this.storyId = storyId;
        this.storyPicture = storyPicture;
        this.text = text;
    }
}
