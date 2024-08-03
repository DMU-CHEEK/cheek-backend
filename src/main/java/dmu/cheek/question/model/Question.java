package dmu.cheek.question.model;

import dmu.cheek.global.auditing.BaseTimeEntity;
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
public class Question extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private long questionId;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name =  "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Story> storyList = new ArrayList<>();

    @Builder(builderMethodName = "withoutPrimaryKey")
    public Question(String content, Member member, Category category) {
        this.content = content;
        this.member = member;
        this.category = category;
    }

    @Builder(builderMethodName = "naturalFields")
    public Question(long questionId, String content) {
        this.questionId = questionId;
        this.content = content;
    }

    public void update(String content) {
        this.content = content;
    }

}
