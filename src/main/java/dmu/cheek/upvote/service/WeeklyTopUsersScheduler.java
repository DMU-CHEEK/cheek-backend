package dmu.cheek.upvote.service;

import dmu.cheek.member.model.Member;
import dmu.cheek.upvote.repository.UpvoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Component
@RequiredArgsConstructor
public class WeeklyTopUsersScheduler {

    private final UpvoteRepository upvoteRepository;

    @Scheduled(cron = "0 0 0 * * MON")
    public void selectTopMembers() {
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)); //mon
        LocalDate endOfWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)); //sun

        Pageable pageable = PageRequest.of(0, 3);
        List<Member> topMembers = upvoteRepository.findTop3MembersWithMostLikesInWeek(startOfWeek, endOfWeek, pageable);
    }
}
