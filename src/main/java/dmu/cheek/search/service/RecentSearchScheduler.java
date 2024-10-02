package dmu.cheek.search.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@EnableScheduling
@RequiredArgsConstructor
@Service
@Slf4j
public class RecentSearchScheduler {

    private final RecentSearchService recentSearchService;

    //2주마다 실행 (밀리초로 설정, 1209600000ms = 2주)
    @Scheduled(fixedRate = 1209600000)
    public void clearOldSearchData() {
        log.info("scheduled task executed: clear all members recent-searches");

        recentSearchService.clearAllRecentSearches();
    }
}
