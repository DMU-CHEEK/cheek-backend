package dmu.cheek.member.service;

import dmu.cheek.member.model.Member;
import dmu.cheek.upvote.repository.UpvoteRepository;
import dmu.cheek.upvote.service.UpvoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Component
@RequiredArgsConstructor
public class WeeklyTopMembersScheduler {

    private final UpvoteService upvoteService;
    private RedisTemplate<String, Object> redisTemplate;

    @Scheduled(cron = "0 0 0 * * MON")
    public void selectTopMembers() {
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)); //mon
        LocalDate endOfWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)); //sun

        Pageable pageable = PageRequest.of(0, 3);
        List<Member> top3MembersWithMostLikesInWeek = upvoteService.findTop3MembersWithMostLikesInWeek(startOfWeek, endOfWeek, pageable);
        redisTemplate.opsForList().rightPushAll("topMembers", top3MembersWithMostLikesInWeek); //redis에 저장
    }
}
