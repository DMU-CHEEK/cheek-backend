package dmu.cheek.search.controller;

import dmu.cheek.search.service.SearchService;
import dmu.cheek.search.service.model.SearchDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
        SearchDto searchDto = searchService.searchAll(keyword, categoryId, loginMemberId);

        return ResponseEntity.ok(searchDto);
    }
}
