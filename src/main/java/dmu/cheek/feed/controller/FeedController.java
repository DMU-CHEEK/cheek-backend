package dmu.cheek.feed.controller;

import dmu.cheek.feed.model.FeedDto;
import dmu.cheek.feed.service.FeedService;
import dmu.cheek.global.resolver.memberInfo.MemberInfo;
import dmu.cheek.global.resolver.memberInfo.MemberInfoDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/feed")
@RequiredArgsConstructor
@Tag(name = "Feed API", description = "피드 기능")
public class FeedController {

    private final FeedService feedService;

    @GetMapping("/{categoryId}")
    @Operation(summary = "피드 조회", description = "피드 조회 API")
    public ResponseEntity<List> getFeed(@PathVariable(name = "categoryId") long categoryId,
                                        @MemberInfo MemberInfoDto memberInfoDto) {
        List<FeedDto> feed = feedService.getFeed(memberInfoDto, categoryId);
        return ResponseEntity.ok(feed);
    }
}