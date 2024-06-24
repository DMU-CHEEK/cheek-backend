package dmu.cheek.question.converter;

import dmu.cheek.question.model.Question;
import dmu.cheek.question.model.QuestionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionConverter {


    public Question convertToEntity(QuestionDto questionDto) {
        if (questionDto == null)
            return null;

        return Question.naturalFields()
                .questionId(questionDto.getQuestionId())
                .content(questionDto.getContent())
                .build();
    }

    public QuestionDto convertToDto(Question question) {
        if (question == null)
            return null;

        return QuestionDto.builder()
                .questionId(question.getQuestionId())
                .content(question.getContent())
                .build();
    }
}
