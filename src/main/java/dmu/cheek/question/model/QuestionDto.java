package dmu.cheek.question.model;

import dmu.cheek.member.model.Member;
import dmu.cheek.member.model.MemberDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class QuestionDto {

    private long questionId;

    private String content;

    private MemberDto member;

    private Category category;

    @Builder(builderMethodName = "allFields")
    public QuestionDto(long questionId, String content, MemberDto member, Category category) {
        this.questionId = questionId;
        this.content = content;
        this.member = member;
        this.category = category;
    }

    @Getter
    public static class Request {
        private String content;
        private long categoryId;
        private long memberId;
    }
}
