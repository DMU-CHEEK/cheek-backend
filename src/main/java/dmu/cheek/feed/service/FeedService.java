package dmu.cheek.feed.service;

import dmu.cheek.feed.model.FeedDto;
import dmu.cheek.global.resolver.memberInfo.MemberInfoDto;
import dmu.cheek.member.model.Member;
import dmu.cheek.member.service.MemberService;
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
    private final MemberService memberService;

    public List<FeedDto> getFeed(MemberInfoDto memberInfoDto, long categoryId) {
        Member member = memberService.findById(memberInfoDto.getMemberId());

        List<FeedDto> questionList = questionService.getQuestionsForFeed(member, categoryId);
        List<FeedDto> storyList = storyService.getStoriesForFeed(member, categoryId);


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