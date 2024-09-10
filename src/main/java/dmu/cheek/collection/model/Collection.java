package dmu.cheek.collection.model;

import dmu.cheek.folder.model.Folder;
import dmu.cheek.global.auditing.BaseTimeEntity;
import dmu.cheek.member.model.Member;
import dmu.cheek.question.model.Category;
import dmu.cheek.story.model.Story;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@NoArgsConstructor
public class Collection extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "collection_id")
    private long collectionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "story_id")
    private Story story;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "folder_id")
    private Folder folder;

    @Builder(builderMethodName = "allFields")
    public Collection(long collectionId, Category category,
                            Member member, Story story, Folder folder) {
        this.collectionId = collectionId;
        this.category = category;
        this.member = member;
        this.story = story;
        this.folder = folder;
    }

    @Builder(builderMethodName = "withoutPrimaryKey")
    public Collection(Category category,
                      Member member, Story story, Folder folder) {
        this.category = category;
        this.member = member;
        this.story = story;
        this.folder = folder;
    }

    @Builder(builderMethodName = "naturalFields")
    public Collection(long collectionId) {
        this.collectionId = collectionId;
    }
}
