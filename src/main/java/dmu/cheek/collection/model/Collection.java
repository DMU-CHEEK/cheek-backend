package dmu.cheek.collection.model;

import dmu.cheek.global.auditing.BaseTimeEntity;
import dmu.cheek.member.model.Member;
import dmu.cheek.question.model.Category;
import dmu.cheek.story.model.Story;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Collection extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "collection_id")
    private long collectionId;

    @Column(name = "thumbnail_picture")
    private String thumbnailPicture;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "story_id")
    private Story story;

    @Builder(builderMethodName = "allFields")
    public Collection(long collectionId, String thumbnailPicture, Category category,
                            Member member, Story story) {
        this.collectionId = collectionId;
        this.thumbnailPicture = thumbnailPicture;
        this.category = category;
        this.member = member;
        this.story = story;
    }

    @Builder(builderMethodName = "withoutPrimaryKey")
    public Collection(String thumbnailPicture, Category category,
                      Member member, Story story) {
        this.thumbnailPicture = thumbnailPicture;
        this.category = category;
        this.member = member;
        this.story = story;
    }
}
