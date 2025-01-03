package dmu.cheek.highlight.model;

import dmu.cheek.global.auditing.BaseTimeEntity;
import dmu.cheek.member.model.Member;
import dmu.cheek.highlight.model.HighlightStory;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Highlight extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "highlight_id")
    private long highlightId;

    private String thumbnailPicture;

    private String subject;

    @OneToMany(mappedBy = "highlight", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<HighlightStory> highlightStoryList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder(builderMethodName = "withoutPrimaryKey")
    public Highlight(String thumbnailPicture, String subject, List<HighlightStory> highlightStoryList, Member member) {
        this.thumbnailPicture = thumbnailPicture;
        this.subject = subject;
        this.highlightStoryList = highlightStoryList;
        this.member = member;
    }

    public void update(String subject, String thumbnailPicture) {
        this.subject = subject;
        this.thumbnailPicture = thumbnailPicture;
    }
}
