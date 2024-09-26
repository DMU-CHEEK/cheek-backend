package dmu.cheek.search.controller;

import dmu.cheek.search.service.SearchService;
import dmu.cheek.search.service.model.SearchDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @GetMapping("/all/{categoryId}")
    public ResponseEntity<SearchDto> searchAll(@PathVariable(name = "categoryId") long categoryId,
                                          @RequestParam(name = "keyword") String keyword,
                                          @RequestParam(name = "loginMemberId") long loginMemberId) {
        SearchDto searchDto = searchService.searchAll(keyword, categoryId, loginMemberId);

        return ResponseEntity.ok(searchDto);
    }
}
