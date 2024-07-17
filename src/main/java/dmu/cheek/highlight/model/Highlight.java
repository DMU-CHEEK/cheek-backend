package dmu.cheek.highlight.model;

import dmu.cheek.member.model.Member;
import dmu.cheek.story.model.Story;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class
Highlight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "highlight_id")
    private long highlightId;

    @Column(name = "thumbnail_picture")
    private String thumbnailPicture;

    @OneToMany(mappedBy = "highlight", fetch = FetchType.LAZY)
    private List<Story> storyList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder(builderMethodName = "withoutPrimaryKey")
    public Highlight(String thumbnailPicture, List<Story> storyList, Member member) {
        this.thumbnailPicture = thumbnailPicture;
        this.storyList = storyList;
        this.member = member;
    }
}
