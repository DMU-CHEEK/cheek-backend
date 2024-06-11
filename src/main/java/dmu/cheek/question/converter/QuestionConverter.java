package dmu.cheek.question.converter;

import dmu.cheek.question.model.Question;
import dmu.cheek.question.model.QuestionDto;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuestionConverter {

    public Question convertToEntity(QuestionDto questionDto) {
        if (questionDto == null)
            return null;

        return Question.allFields()
                .questionId(questionDto.getQuestionId())
                .content(questionDto.getContent())
                .member(questionDto.getMember())
                .category(questionDto.getCategory())
                .build();
    }

    public QuestionDto convertToDto(Question question) {
        if (question == null)
            return null;

        return QuestionDto.allFields()
                .questionId(question.getQuestionId())
                .content(question.getContent())
                .member(question.getMember())
                .category(question.getCategory())
                .build();
    }
}
