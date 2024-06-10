package dmu.cheek.question.model;

import dmu.cheek.member.model.Member;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private long questionId;

    private String content;

    @ManyToOne
    @JoinColumn(name =  "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "profession_id")
    private Profession profession;
}
