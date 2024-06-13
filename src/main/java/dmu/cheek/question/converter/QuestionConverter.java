package dmu.cheek.question.converter;

import dmu.cheek.member.converter.MemberConverter;
import dmu.cheek.member.model.Member;
import dmu.cheek.member.model.MemberDto;
import dmu.cheek.question.model.Question;
import dmu.cheek.question.model.QuestionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionConverter {

    private final MemberConverter memberConverter;

    public Question convertToEntity(QuestionDto questionDto) {
        if (questionDto == null)
            return null;

        Member member = memberConverter.convertToEntity(questionDto.getMember());

        return Question.allFields()
                .questionId(questionDto.getQuestionId())
                .content(questionDto.getContent())
                .member(member)
                .category(questionDto.getCategory())
                .build();
    }

    public QuestionDto convertToDto(Question question) {
        if (question == null)
            return null;

        MemberDto memberDto = memberConverter.convertToDto(question.getMember());

        return QuestionDto.allFields()
                .questionId(question.getQuestionId())
                .content(question.getContent())
                .member(memberDto)
                .category(question.getCategory())
                .build();
    }
}
