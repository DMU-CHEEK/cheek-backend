package dmu.cheek.collection.model;

import dmu.cheek.member.model.Member;
import dmu.cheek.question.model.Category;
import dmu.cheek.story.model.Story;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Collection {

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

    @OneToMany(mappedBy = "collection", fetch = FetchType.LAZY)
    private List<Story> storyList = new ArrayList<>();




}
