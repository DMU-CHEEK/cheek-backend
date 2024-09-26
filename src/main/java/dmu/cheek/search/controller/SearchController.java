package dmu.cheek.search.controller;

import dmu.cheek.search.model.SearchDto;
import dmu.cheek.search.service.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
@Tag(name = "Search API", description = "검색 기능")
public class SearchController {

    private final SearchService searchService;

    @GetMapping("/all/{categoryId}")
    @Operation(summary = "전체 검색", description = "전체 검색 API")
    public ResponseEntity<SearchDto> searchAll(@PathVariable(name = "categoryId") long categoryId,
                                          @RequestParam(name = "keyword") String keyword,
                                          @RequestParam(name = "loginMemberId") long loginMemberId) {
        SearchDto searchDto = searchService.searchAll(keyword, loginMemberId, categoryId);

        log.info("search member result: {}", searchDto.getMemberDto().size());
        log.info("search story result: {}", searchDto.getStoryDto().size());
        log.info("search question result: {}", searchDto.getQuestionDto().size());

        return ResponseEntity.ok(searchDto);
    }
}
