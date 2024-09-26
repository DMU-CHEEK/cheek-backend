package dmu.cheek.story.repository;

import dmu.cheek.story.model.Story;

import java.util.List;

public interface StoryRepositoryCustom {

    List<Story> findListByCategoryAndText(long categoryId, String keyword);
}
