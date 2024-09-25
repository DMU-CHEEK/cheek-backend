package dmu.cheek.member.service;

import dmu.cheek.member.converter.MemberConverter;
import dmu.cheek.member.model.MemberDto;
import dmu.cheek.upvote.service.UpvoteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class WeeklyTopMembersScheduler {

    private final UpvoteService upvoteService;
    private final MemberConverter memberConverter;
    private final RedisTemplate<String, Object> redisTemplate;

    @Scheduled(cron = "0 0 0 * * MON")
    public void selectTopMembers() {
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.minusWeeks(1).with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfWeek = today.minusWeeks(1).with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
        LocalDateTime startOfWeekDateTime = startOfWeek.atStartOfDay();
        LocalDateTime endOfWeekDateTime = endOfWeek.atTime(23, 59, 59);
        Pageable pageable = PageRequest.of(0, 3);

        List<MemberDto> topMembers = upvoteService.findTop3MembersWithMostLikesInWeek(startOfWeekDateTime, endOfWeekDateTime, pageable)
                .stream()
                .map(memberConverter::convertToDto)
                .toList();

        List<MemberDto.Top3MemberInfo> top3MemberInfoList = topMembers.stream()
                .map(memberDto ->
                        MemberDto.Top3MemberInfo.builder()
                                .memberId(memberDto.getMemberId())
                                .profilePicture(memberDto.getProfilePicture())
                                .description(memberDto.getDescription())
                                .information(memberDto.getInformation())
                                .nickname(memberDto.getNickname())
                                .build()
                ).toList();

        redisTemplate.delete("topMembers");
        redisTemplate.opsForList().rightPushAll("topMembers", top3MemberInfoList);

        log.info("scheduled task executed: save top 3 upvoted members' info");
    }
}
