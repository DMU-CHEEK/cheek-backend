package dmu.cheek.question.repository;

import dmu.cheek.member.model.Member;
import dmu.cheek.question.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    @Query("select q from Question q where q.member = :member")
    List<Question> findByMember(Member member);
}
