package dmu.cheek.feed.service;

import dmu.cheek.feed.model.FeedDto;
import dmu.cheek.question.service.QuestionService;
import dmu.cheek.story.service.StoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class FeedService {

    private final QuestionService questionService;
    private final StoryService storyService;

    public List<FeedDto> getFeed(long loginMemberId, long categoryId) {
        List<FeedDto> questionList = questionService.getQuestionsForFeed(categoryId);
        List<FeedDto> storyList = storyService.getStoriesForFeed(loginMemberId, categoryId);


        log.info("get feed, categoryId: {}", categoryId);

        return Stream.concat(questionList.stream(), storyList.stream())
                .sorted((o1, o2) -> {
                    LocalDateTime date1 = o1.getModifiedAt();
                    LocalDateTime date2 = o2.getModifiedAt();

                    return date2.compareTo(date1);
                })
                .toList();
    }
}