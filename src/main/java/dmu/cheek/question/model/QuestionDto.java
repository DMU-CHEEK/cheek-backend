package dmu.cheek.question.model;

import dmu.cheek.member.model.MemberDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
        @Size(max = 50, message = "content must be between 1 and 50 characters")
        @NotBlank(message = "content cannot be blank")
        private String content;

        private long categoryId;

        private long memberId;
    }

    @Getter
    public static class UpdateReq {
        @Size(max = 50, message = "content must be between 1 and 50 characters")
        @NotBlank(message = "content cannot be blank")
        private String content;

        private long questionId;
    }

    @Getter @Builder
    public static class ResponseList {
        private long questionId;
        private String content;
        private int storyCnt;
    }

    @Getter @Builder
    public static class ResponseOne {
        private long questionId;
        private String content;
        private long categoryId;
        private MemberDto.Concise memberDto;
    }
}
