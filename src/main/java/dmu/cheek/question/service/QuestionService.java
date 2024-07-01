package dmu.cheek.question.service;

import dmu.cheek.global.error.ErrorCode;
import dmu.cheek.global.error.exception.BusinessException;
import dmu.cheek.member.converter.MemberConverter;
import dmu.cheek.member.model.Member;
import dmu.cheek.member.model.MemberDto;
import dmu.cheek.member.service.MemberService;
import dmu.cheek.question.converter.CategoryConverter;
import dmu.cheek.question.converter.QuestionConverter;
import dmu.cheek.question.model.Category;
import dmu.cheek.question.model.Question;
import dmu.cheek.question.model.QuestionDto;
import dmu.cheek.question.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    @Transactional
    public void register(QuestionDto.RegisterReq registerReq) {
        Member member = memberService.findById(registerReq.getMemberId());
        Category category = categoryService.findById(registerReq.getCategoryId());

        Question question = Question.withoutPrimaryKey()
                .content(registerReq.getContent())
                .member(member)
                .category(category)
                .build();

        questionRepository.save(question);

        log.info("register new question: {}, memberId: {}", question.getQuestionId(), member.getMemberId());
    }

    public List<QuestionDto.Response> searchByMember(long memberId) {
        Member member = memberService.findById(memberId);
        List<Question> questionList = questionRepository.findByMember(member);

        List<QuestionDto.Response> responseList = questionList.stream()
                .map(q -> QuestionDto.Response.builder()
                        .questionId(q.getQuestionId())
                        .content(q.getContent())
                        .memberDto(memberConverter.convertToDto(member))
                        .categoryDto(categoryConverter.convertToDto(q.getCategory()))
                        .build())
                .collect(Collectors.toList());

        log.info("question list by member: {}, number of posts: {}", memberId, responseList.size());

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
}