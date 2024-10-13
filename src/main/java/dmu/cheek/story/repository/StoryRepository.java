package dmu.cheek.story.repository;

import dmu.cheek.member.model.Member;
import dmu.cheek.story.model.Story;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StoryRepository extends JpaRepository<Story, Long>, StoryRepositoryCustom {

    @Query("select s from Story s where s.member = :member order by s.storyId desc")
    List<Story> findByMemberOrderByIdDesc(Member member);

    @Override
    List<Story> findByCategoryIdAndText(long categoryId, String keyword);

    @Query("select s from Story s where s.category.categoryId = :categoryId order by s.modifiedAt desc")
    List<Story> findByCategoryIdOrderByModifiedAtDesc(long categoryId);
}
