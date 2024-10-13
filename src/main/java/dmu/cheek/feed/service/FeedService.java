package dmu.cheek.feed.service;

import dmu.cheek.feed.model.FeedDto;
import dmu.cheek.question.service.QuestionService;
import dmu.cheek.story.service.StoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class FeedService {

    private final QuestionService questionService;
    private final StoryService storyService;

    public FeedDto getFeed(long loginMemberId, long categoryId) {
        List<FeedDto.Question> questionList = questionService.getQuestionsForFeed(categoryId);
        List<FeedDto.Story> storyList = storyService.getStoriesForFeed(loginMemberId, categoryId);

        log.info("get feed, categoryId: {}", categoryId);

        return FeedDto.builder()
                .storyDto(storyList)
                .questionDto(questionList)
                .build();
    }
}