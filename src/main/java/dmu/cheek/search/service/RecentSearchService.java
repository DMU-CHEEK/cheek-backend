package dmu.cheek.search.service;

import dmu.cheek.search.model.SearchDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class RecentSearchService {

    private final RedisTemplate<String, String> redisTemplate;

    public void addRecentSearch(long memberId, String keyword) {
        String key = "member:" + memberId + ":recent-searches";

        redisTemplate.opsForList().remove(key, 0, keyword); //중복값 삭제
        redisTemplate.opsForList().leftPush(key, keyword);
        redisTemplate.opsForList().trim(key, 0, 9); //최대 10개 검색어

        log.info("register recent-search, memberId: {}, keyword: {}", memberId, keyword);
    }

    public SearchDto.Keyword getRecentSearches(long memberId) {
        String key = "member:" + memberId + ":recent-searches";
        List<String> recentSearches = redisTemplate.opsForList().range(key, 0, -1); //최신순 조회

        log.info("get recent-searches, memberId: {}", memberId);

        return SearchDto.Keyword
                .builder()
                .keyword(recentSearches)
                .build();
    }

    public void clearAllRecentSearches() {
            Set<String> keys = redisTemplate.keys("member:*:recent-searches");

            if (keys != null && !keys.isEmpty())
                redisTemplate.delete(keys);
    }
}
