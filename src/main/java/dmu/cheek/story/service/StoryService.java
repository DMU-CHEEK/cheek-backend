package dmu.cheek.story.service;

import dmu.cheek.feed.model.FeedDto;
import dmu.cheek.global.error.ErrorCode;
import dmu.cheek.global.error.exception.BusinessException;
import dmu.cheek.member.model.Member;
import dmu.cheek.member.model.MemberDto;
import dmu.cheek.member.service.MemberService;
import dmu.cheek.noti.model.Notification;
import dmu.cheek.noti.model.Type;
import dmu.cheek.noti.service.NotificationService;
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

import java.util.List;

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
    private final NotificationService notificationService;

    @Transactional
    public void register(MultipartFile storyPicture, StoryDto.Request storyDto) {
        Member member = memberService.findById(storyDto.getMemberId());
        Question question = questionService.findById(storyDto.getQuestionId());
        Category category = categoryService.findById(storyDto.getCategoryId());

        S3Dto s3Dto = s3Service.saveFile(storyPicture);
        Story story = Story.builder()
                .storyPicture(s3Dto.getStoreFileName())
                .text(storyDto.getText().toLowerCase())
                .member(member)
                .category(category)
                .question(question)
                .build();

        storyRepository.save(story);
        log.info("register new story: {}, memberId: {}, questionId: {}", story.getStoryId(), story.getMember().getMemberId(), story.getQuestion().getQuestionId());

        //send notification
        String notiBody = String.format("%s님이 내 질문에 스토리로 답변했어요.", member.getNickname());

        notificationService.register(
                Notification.withoutPrimaryKey()
                        .body(notiBody)
                        .type(Type.from("STORY"))
                        .typeId(story.getStoryId())
                        .fromMember(member)
                        .toMember(memberService.findById(story.getQuestion().getMember().getMemberId()))
                        .build()
        );
    }

    @Transactional
    public void delete(StoryDto.Delete storyDto) {
        for (long storyId : storyDto.getStoryIdList()) {
            Story story = storyRepository.findById(storyId)
                    .orElseThrow(() -> new BusinessException(ErrorCode.STORY_NOT_FOUND));

            storyRepository.delete(story);
        }

        log.info("delete story list");
    }

    public void deleteOne(long storyId) {

    }

    public List<StoryDto.ResponseList> searchListByMember(long loginMemberId, long targetMemberId) {
        //loginMemberId: 조회하는 유저, targetMemberId: 스토리를 조회할 대상 유저
        Member targetMember = memberService.findById(targetMemberId);

        List<Story> storyList = storyRepository.findByMemberOrderByIdDesc(targetMember);

        log.info("search story list by memberId: {}", targetMemberId);

        return storyList.stream()
                .map(story -> StoryDto.ResponseList.builder()
                        .storyId(story.getStoryId())
                        .categoryId(story.getCategory().getCategoryId())
                        .storyPicture(s3Service.getResourceUrl(story.getStoryPicture()))
                        .isUpvoted(story.getUpvoteList().stream()
                                .anyMatch(upvote -> upvote.getMember().getMemberId() == loginMemberId))
                        .upvoteCount(story.getUpvoteList().size())
                        .modifiedAt(story.getModifiedAt())
                        .build())
                .toList();
    }

    public StoryDto.ResponseOne searchByMember(long storyId, long loginMemberId) {
        Story story = storyRepository.findById(storyId)
                .orElseThrow(() -> new BusinessException(ErrorCode.STORY_NOT_FOUND));

        log.info("search story by storyId: {}", storyId);

        return StoryDto.ResponseOne.builder()
                .storyId(storyId)
                .storyPicture(s3Service.getResourceUrl(story.getStoryPicture()))
                .categoryId(story.getCategory().getCategoryId())
                .isUpvoted(story.getUpvoteList().stream()
                        .anyMatch(upvote -> upvote.getMember().getMemberId() == loginMemberId))
                .upvoteCount(story.getUpvoteList().size())
                .memberDto(MemberDto.Concise.builder()
                        .memberId(story.getMember().getMemberId())
                        .profilePicture(s3Service.getResourceUrl(story.getMember().getProfilePicture()))
                        .nickname(story.getMember().getNickname())
                        .build()
                )
                .build();
    }

    public Story findById(long storyId) {
        return storyRepository.findById(storyId)
                .orElseThrow(() -> new BusinessException(ErrorCode.STORY_NOT_FOUND));
    }

    public List<FeedDto> getStoriesForFeed(long loginMemberId, long categoryId) {
        return storyRepository.findByCategoryId(categoryId)
                .stream()
                .map(story ->
                        FeedDto.builder()
                                .type("STORY")
                                .memberDto(MemberDto.Concise.builder()
                                        .memberId(story.getMember().getMemberId())
                                        .profilePicture(s3Service.getResourceUrl(story.getMember().getProfilePicture()))
                                        .nickname(story.getMember().getNickname())
                                        .build()
                                )
                                .storyDto(FeedDto.Story.builder()
                                        .storyId(story.getStoryId())
                                        .storyPicture(s3Service.getResourceUrl(story.getStoryPicture()))
                                        .isUpvoted(story.getUpvoteList().stream()
                                                .anyMatch(upvote -> upvote.getMember().getMemberId() == loginMemberId)
                                        )
                                        .upvoteCount((int) story.getUpvoteList().size())
                                        .build()
                                ).questionDto(null)
                                .modifiedAt(story.getModifiedAt())
                                .build()
                ).toList();
    }

    public List<StoryDto.ResponseList> getAnsweredStoryList(long questionId, long loginMemberId) {
        Question question = questionService.findById(questionId);

        log.info("get answered story list, questionId: {}", questionId);

        return question.getStoryList().stream()
                .map(story -> StoryDto.ResponseList.builder()
                        .storyId(story.getStoryId())
                        .storyPicture(story.getStoryPicture())
                        .categoryId(story.getCategory().getCategoryId())
                        .isUpvoted(story.getUpvoteList().stream()
                                .anyMatch(upvote -> upvote.getMember().getMemberId() == loginMemberId))
                        .upvoteCount(story.getUpvoteList().size())
                        .build()
                ).toList();
    }
}
