package dmu.cheek.search.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import dmu.cheek.member.model.Member;
import dmu.cheek.member.model.QMember;
import dmu.cheek.member.service.MemberService;
import dmu.cheek.question.model.QQuestion;
import dmu.cheek.question.model.Question;
import dmu.cheek.s3.service.S3Service;
import dmu.cheek.search.service.model.SearchDto;
import dmu.cheek.story.model.QStory;
import dmu.cheek.story.model.Story;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class SearchService {

    private final JPAQueryFactory jpaQueryFactory;
    private final MemberService memberService;
    private final S3Service s3Service;

    public SearchDto searchAll(String keyword, long categoryId, long loginMemberId) {
        List<SearchDto.Member> memberList = searchMember(keyword, categoryId, loginMemberId);
//        List<SearchDto.Story> storyList = searchStory(keyword, categoryId);
        List<SearchDto.Question> questionList = searchQuestion(keyword, categoryId);

        log.info("search all, keyword: {}", keyword);

        return SearchDto.builder()
                .memberDto(memberList)
//                .storyDto(storyList)
                .questionDto(questionList)
                .build();
    }

    public List<SearchDto.Member> searchMember(String keyword, long categoryId, long loginMemberId) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QMember member = QMember.member;
        booleanBuilder.or(member.nickname.containsIgnoreCase(keyword));

        List<Member> memberList = jpaQueryFactory
                .selectFrom(member)
                .where(booleanBuilder)
                .fetch();

        log.info("search in members, keyword: {}", keyword);

        return memberList.stream()
                .map(memberData -> {
                    long followerCnt = memberData.getToMemberConnectionList().size();
                    boolean isFollowing = memberData.getToMemberConnectionList()
                            .stream()
                            .anyMatch(memberConnection -> memberConnection.getFromMember().getMemberId() == loginMemberId);

                    return SearchDto.Member.builder()
                            .memberId(memberData.getMemberId())
                            .profilePicture(s3Service.getResourceUrl(memberData.getProfilePicture()))
                            .description(memberData.getDescription())
                            .information(memberData.getInformation())
                            .isFollowing(isFollowing)
                            .followerCnt(followerCnt)
                            .resultCnt(memberList.size())
                            .build();
                })
                .toList();

    }

//    public List<SearchDto.Story> searchStory(String keyword, long categoryId) {
//        BooleanBuilder booleanBuilder = new BooleanBuilder();

        //        QStory story = QStory.story;
        //        booleanBuilder.or(story.contentString.containsIgnoreCase(keyword));
        //        List<Story> storyList = jpaQueryFactory.selectFrom(story).where(builder).fetch();

//        log.info("search in stories, keyword: {}", keyword);
//    }

    public List<SearchDto.Question> searchQuestion(String keyword, long categoryId) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QQuestion question = QQuestion.question;

        if (categoryId > 0)
            booleanBuilder.and(question.category.categoryId.eq(categoryId));

        booleanBuilder.or(question.content.containsIgnoreCase(keyword));

        List<Question> questionList = jpaQueryFactory
                .selectFrom(question)
                .where(booleanBuilder)
                .fetch();

        log.info("search in questions, keyword: {}", keyword);

        return questionList
                .stream()
                .map(questionData -> SearchDto.Question.builder()
                        .questionId(questionData.getQuestionId())
                        .content(questionData.getContent())
                        .resultCnt(questionList.size())
                        .build())
                .toList();

    }
}
