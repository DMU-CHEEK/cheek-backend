package dmu.cheek.question.service;

import dmu.cheek.feed.model.FeedDto;
import dmu.cheek.global.error.ErrorCode;
import dmu.cheek.global.error.exception.BusinessException;
import dmu.cheek.global.resolver.memberInfo.MemberInfoDto;
import dmu.cheek.member.converter.MemberConverter;
import dmu.cheek.member.model.Member;
import dmu.cheek.member.model.MemberDto;
import dmu.cheek.member.service.MemberService;
import dmu.cheek.question.converter.CategoryConverter;
import dmu.cheek.question.model.Category;
import dmu.cheek.question.model.Question;
import dmu.cheek.question.model.QuestionDto;
import dmu.cheek.question.repository.QuestionRepository;
import dmu.cheek.s3.service.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final MemberService memberService;
    private final MemberConverter memberConverter;
    private final CategoryConverter categoryConverter;
    private final CategoryService categoryService;
    private final S3Service s3Service;

    @Transactional
    public void register(QuestionDto.RegisterReq registerReq, MemberInfoDto memberInfoDto) {
        Member member = memberService.findById(memberInfoDto.getMemberId());

        Category category = categoryService.findById(registerReq.getCategoryId());

        Question question = Question.withoutPrimaryKey()
                .content(registerReq.getContent())
                .member(member)
                .category(category)
                .build();

        questionRepository.save(question);

        log.info("register new question: {}, memberId: {}", question.getQuestionId(), member.getMemberId());
    }

    public QuestionDto.ResponseOne search(long questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new BusinessException(ErrorCode.QUESTION_NOT_FOUND));

        log.info("get question by id: {}", questionId);

        return QuestionDto.ResponseOne.builder()
                .questionId(question.getQuestionId())
                .content(question.getContent())
                .categoryId(question.getCategory().getCategoryId())
                .modifiedAt(question.getModifiedAt())
                .memberDto(
                        MemberDto.Concise.builder()
                                .memberId(question.getMember().getMemberId())
                                .nickname(question.getMember().getNickname())
                                .profilePicture(s3Service.getResourceUrl(question.getMember().getProfilePicture()))
                                .build()
                )
                .build();
    }

    public List<QuestionDto.ResponseList> searchByMember(long memberId) {
        Member member = memberService.findById(memberId);
        List<Question> questionList = questionRepository.findByMemberOrderByModifiedAtDesc(member);

        List<QuestionDto.ResponseList> responseList = questionList.stream()
                .map(q -> QuestionDto.ResponseList.builder()
                        .questionId(q.getQuestionId())
                        .content(q.getContent())
                        .storyCnt(q.getStoryList().size())
                        .build())
                .toList();

        log.info("get question list by member: {}, number of posts: {}", memberId, responseList.size());

        return responseList;
    }

    @Transactional
    public void update(QuestionDto.UpdateReq updateReq) {
        Question question = questionRepository.findById(updateReq.getQuestionId())
                .orElseThrow(() -> new BusinessException(ErrorCode.QUESTION_NOT_FOUND));

        question.update(updateReq.getContent());

        log.info("update question with questionId: {}", updateReq.getQuestionId());
    }

    public Question findById(long questionId) {
        return questionRepository.findById(questionId)
                .orElseThrow(() -> new BusinessException(ErrorCode.QUESTION_NOT_FOUND));
    }

    public List<FeedDto> getQuestionsForFeed(Member member, long categoryId) {

        return questionRepository.findByCategoryId(categoryId)
                .stream()
                .filter(question -> {
                    boolean isBlocked = member.getFromBlockList().stream()
                            .anyMatch(blockedMember ->
                                    blockedMember.getToMember().getMemberId() == question.getMember().getMemberId()
                            );
                    return !isBlocked;
                })
                .map(question ->
                        FeedDto.builder()
                                .type("QUESTION")
                                .memberDto(MemberDto.Concise.builder()
                                        .memberId(question.getMember().getMemberId())
                                        .profilePicture(s3Service.getResourceUrl(question.getMember().getProfilePicture()))
                                        .nickname(question.getMember().getNickname())
                                        .build()
                                )
                                .questionDto(FeedDto.Question.builder()
                                        .questionId(question.getQuestionId())
                                        .content(question.getContent())
                                        .storyCnt(question.getStoryList().size())
                                        .build()
                                )
                                .storyDto(null)
                                .modifiedAt(question.getModifiedAt())
                                .build()
                ).toList();
    }
}
