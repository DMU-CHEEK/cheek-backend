package dmu.cheek.question.service;

import dmu.cheek.global.error.ErrorCode;
import dmu.cheek.global.error.exception.BusinessException;
import dmu.cheek.member.model.Member;
import dmu.cheek.member.model.MemberDto;
import dmu.cheek.member.service.MemberService;
import dmu.cheek.question.model.Category;
import dmu.cheek.question.model.CategoryDto;
import dmu.cheek.question.model.Question;
import dmu.cheek.question.model.QuestionDto;
import dmu.cheek.question.repository.CategoryRepository;
import dmu.cheek.question.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final MemberService memberService;
    private final CategoryService categoryService;

    @Transactional
    public void register(QuestionDto.Request questionRequestDto) {
        Member member = memberService.findById(questionRequestDto.getMemberId());
        Category category = categoryService.findById(questionRequestDto.getCategoryId());

        Question question = Question.withoutPrimaryKey()
                .content(questionRequestDto.getContent())
                .member(member)
                .category(category)
                .build();

        questionRepository.save(question);

        log.info("register new question: {}", question.getQuestionId());
    }

}
