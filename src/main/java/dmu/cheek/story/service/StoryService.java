package dmu.cheek.story.service;

import dmu.cheek.member.model.Member;
import dmu.cheek.member.service.MemberService;
import dmu.cheek.question.model.Category;
import dmu.cheek.question.model.Question;
import dmu.cheek.question.service.CategoryService;
import dmu.cheek.question.service.QuestionService;
import dmu.cheek.s3.model.S3Dto;
import dmu.cheek.s3.service.S3Service;
import dmu.cheek.story.model.Story;
import dmu.cheek.story.model.StoryDto;
import dmu.cheek.story.repository.StoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoryService {

    private final StoryRepository storyRepository;
    private final S3Service s3Service;
    private final MemberService memberService;
    private final QuestionService questionService;
    private final CategoryService categoryService;

    @Transactional
    public void register(MultipartFile storyPicture, StoryDto.Request storyDto) {
        Member member = memberService.findById(storyDto.getMemberId());
        Question question = questionService.findById(storyDto.getQuestionId());
        Category category = categoryService.findById(storyDto.getCategoryId());

        S3Dto s3Dto = s3Service.saveFile(storyPicture);
        Story story = Story.builder()
                .storyPicture(s3Dto.getStoreFileName())
                .member(member)
                .category(category)
                .question(question)
                .build();

        storyRepository.save(story);
        log.info("register new story: {}, memberId: {}, questionId: {}", story.getStoryId(), story.getMember().getMemberId(), story.getQuestion().getQuestionId());
    }
}
