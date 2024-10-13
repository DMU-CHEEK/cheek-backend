package dmu.cheek.story.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import dmu.cheek.story.model.Story;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static dmu.cheek.story.model.QStory.story;

@RequiredArgsConstructor
public class StoryRepositoryImpl implements StoryRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Story> findByCategoryIdAndText(long categoryId, String keyword) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if (categoryId > 0)
            booleanBuilder.and(story.category.categoryId.eq(categoryId));
        booleanBuilder.and(story.text.contains(keyword.toLowerCase()));

        return jpaQueryFactory.selectFrom(story)
                .where(booleanBuilder)
                .fetch();
    }
}
