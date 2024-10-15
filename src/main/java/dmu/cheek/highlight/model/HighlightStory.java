package dmu.cheek.highlight.model;

import dmu.cheek.global.auditing.BaseTimeEntity;
import dmu.cheek.story.model.Story;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class HighlightStory extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long highlightStoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "highlight_id")
    private Highlight highlight;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "story_id")
    private Story story;

    @Builder(builderMethodName = "withoutPrimaryKey")
    public HighlightStory(Highlight highlight, Story story) {
        this.highlight = highlight;
        this.story = story;
    }
}
