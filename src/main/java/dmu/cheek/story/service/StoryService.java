package dmu.cheek.story.service;

import dmu.cheek.global.error.ErrorCode;
import dmu.cheek.global.error.exception.BusinessException;
import dmu.cheek.member.model.Member;
import dmu.cheek.member.service.MemberService;
import dmu.cheek.question.model.Category;
import dmu.cheek.question.model.Question;
import dmu.cheek.question.service.CategoryService;
import dmu.cheek.question.service.QuestionService;
import dmu.cheek.s3.model.S3Dto;
import dmu.cheek.s3.service.S3Service;
import dmu.cheek.story.converter.StoryConverter;
import dmu.cheek.story.model.Story;
import dmu.cheek.story.model.StoryDto;
import dmu.cheek.story.repository.StoryRepository;
import dmu.cheek.upvote.model.Upvote;
import dmu.cheek.upvote.service.UpvoteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Transactional
    public void delete(long storyId) {
        Story story = storyRepository.findById(storyId)
                .orElseThrow(() -> new BusinessException(ErrorCode.STORY_NOT_FOUND));
        storyRepository.delete(story);

        log.info("delete story: {}", story.getStoryId());
    }

    public List<StoryDto.Response> searchListByMember(long loginMemberId, long targetMemberId) {
        //loginMemberId: 조회하는 유저, targetMemberId: 스토리를 조회할 대상 유저
        Member targetMember = memberService.findById(targetMemberId);

        List<Story> storyList = storyRepository.findByMember(targetMember);

        log.info("search story list by memberId: {}", targetMemberId);

        return storyList.stream()
                .map(s -> StoryDto.Response.builder()
                        .storyId(s.getStoryId())
                        .categoryId(s.getCategory().getCategoryId())
                        .storyPicture(s3Service.getResourceUrl(s.getStoryPicture()))
                        .isUpvoted(s.getUpvoteList().stream()
                                .anyMatch(u -> u.getMember().getMemberId() == loginMemberId))
                        .upvoteCount((int) s.getUpvoteList().stream().count())
                        .build())
                .collect(Collectors.toList());
    }

    public StoryDto.Response searchByMember(long storyId, long loginMemberId) {
        Story story = storyRepository.findById(storyId)
                .orElseThrow(() -> new BusinessException(ErrorCode.STORY_NOT_FOUND));

        log.info("search story by storyId: {}", storyId);

        return StoryDto.Response.builder()
                .storyId(storyId)
                .storyPicture(s3Service.getResourceUrl(story.getStoryPicture()))
                .categoryId(story.getCategory().getCategoryId())
                .isUpvoted(story.getUpvoteList().stream()
                        .anyMatch(u -> u.getMember().getMemberId() == loginMemberId))
                .upvoteCount((int) story.getUpvoteList().stream().count())
                .build();
    }

    public Story findById(long storyId) {
        return storyRepository.findById(storyId)
                .orElseThrow(() -> new BusinessException(ErrorCode.STORY_NOT_FOUND));
    }
}
