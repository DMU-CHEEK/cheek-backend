package dmu.cheek.feed.controller;

import dmu.cheek.feed.model.FeedDto;
import dmu.cheek.feed.service.FeedService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/feed")
@RequiredArgsConstructor
@Tag(name = "Feed API", description = "피드 기능")
public class FeedController {

    private final FeedService feedService;

    @GetMapping()
    @Operation(summary = "피드 조회", description = "피드 조회 API")
    public ResponseEntity<FeedDto> getFeed(@RequestParam(name = "loginMemberId") long loginMemberId) {
        FeedDto feed = feedService.getFeed(loginMemberId);

        return ResponseEntity.ok(feed);
    }
}
