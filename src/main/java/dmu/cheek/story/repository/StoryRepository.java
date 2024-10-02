package dmu.cheek.story.repository;

import dmu.cheek.member.model.Member;
import dmu.cheek.story.model.Story;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StoryRepository extends JpaRepository<Story, Long>, StoryRepositoryCustom {

    @Query("select s from Story s where s.member = :member")
    List<Story> findByMember(Member member);

    @Query("select s from Story s where s.category.categoryId = :categoryId and s.text like '%:keyword%'")
    List<Story> findByCategoryAndText(long categoryId, String keyword);

    @Override
    List<Story> findListByCategoryAndText(long categoryId, String keyword);

    @Query("select s from Story s order by s.modifiedAt desc")
    List<Story> findAllByModifiedAtDesc();
}
