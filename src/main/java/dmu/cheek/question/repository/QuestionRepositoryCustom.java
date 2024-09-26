package dmu.cheek.question.repository;

import dmu.cheek.question.model.Question;

import java.util.List;

public interface QuestionRepositoryCustom {

    List<Question> findListByCategoryAndText(long categoryId, String keyword);
}
