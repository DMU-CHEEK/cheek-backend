package dmu.cheek.search.controller;

import dmu.cheek.search.model.SearchDto;
import dmu.cheek.search.service.RecentSearchService;
import dmu.cheek.search.service.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
@Tag(name = "Search API", description = "검색 기능")
public class SearchController {

    private final SearchService searchService;
    private final RecentSearchService recentSearchService;

    @GetMapping("/all/{categoryId}")
    @Operation(summary = "전체 검색", description = "전체 검색 API")
    public ResponseEntity<SearchDto> searchAll(@PathVariable(name = "categoryId") long categoryId,
                                          @RequestParam(name = "keyword") String keyword,
                                          @RequestParam(name = "loginMemberId") long loginMemberId) {
        recentSearchService.addRecentSearch(loginMemberId, keyword);

        SearchDto searchDto = searchService.searchAll(keyword, loginMemberId, categoryId);

        return ResponseEntity.ok(searchDto);
    }

    @GetMapping("/recent")
    @Operation(summary = "최근 검색어 조회", description = "최근 검색어 조회 API")
    public ResponseEntity<List> getRecentSearches(@RequestParam(name = "loginMemberId") long loginMemberId) {

        List<String> recentSearches = recentSearchService.getRecentSearches(loginMemberId);

        return ResponseEntity.ok(recentSearches);
    }
}
