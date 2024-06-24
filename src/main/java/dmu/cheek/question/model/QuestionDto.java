package dmu.cheek.question.model;

import dmu.cheek.member.model.MemberDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionDto {

    private long questionId;

    private String content;


    @Getter
    public static class RegisterReq {
        private String content;
        private long categoryId;
        private long memberId;
    }

    @Getter
    public static class UpdateReq {
        private String content;
        private long questionId;
    }

    @Getter @Builder
    public static class Response {
        private long questionId;
        private String content;
        private MemberDto memberDto;
        private CategoryDto categoryDto;
    }
}
