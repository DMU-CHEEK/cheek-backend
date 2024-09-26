package dmu.cheek.question.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import dmu.cheek.question.model.QQuestion;
import dmu.cheek.question.model.Question;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static dmu.cheek.question.model.QQuestion.question;
import static dmu.cheek.story.model.QStory.story;

@RequiredArgsConstructor
public class QuestionRepositoryImpl implements QuestionRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Question> findListByCategoryAndText(long categoryId, String keyword) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if (categoryId > 0)
            booleanBuilder.and(question.category.categoryId.eq(categoryId));
        booleanBuilder.and(question.content.contains(keyword.toLowerCase()));

        return jpaQueryFactory.selectFrom(question)
                .where(booleanBuilder)
                .fetch();
    }
}
