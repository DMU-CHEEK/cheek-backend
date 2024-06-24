package dmu.cheek.question.model;

import dmu.cheek.member.model.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Question {

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

}
