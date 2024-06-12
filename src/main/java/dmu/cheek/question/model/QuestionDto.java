package dmu.cheek.question.model;

import dmu.cheek.member.model.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class QuestionDto {

    private long questionId;

    private String content;

    private Member member;

    private Category category;

    @Builder(builderMethodName = "allFields")
    public QuestionDto(long questionId, String content, Member member, Category category) {
        this.questionId = questionId;
        this.content = content;
        this.member = member;
        this.category = category;
    }

    @Getter
    public class Request {
        private String content;
        private long categoryId;
        private long memberId;
    }
}
