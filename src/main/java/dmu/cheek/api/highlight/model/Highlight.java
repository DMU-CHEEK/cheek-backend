package dmu.cheek.api.highlight.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Highlight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "highlight_id")
    private long highlightId;

    @Column(name = "thumbnail_picture")
    private String thumbnailPicture;

    @Column(name = "story_id")
    private long storyId;

    @Builder
    public void Highlight(String thumbnailPicture, long storyId) {
        this.thumbnailPicture = thumbnailPicture;
        this.storyId = storyId;
    }
}
