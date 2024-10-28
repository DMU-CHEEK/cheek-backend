package dmu.cheek.upvote.controller;

import dmu.cheek.global.resolver.memberInfo.MemberInfo;
import dmu.cheek.global.resolver.memberInfo.MemberInfoDto;
import dmu.cheek.upvote.model.UpvoteDto;
import dmu.cheek.upvote.service.UpvoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/upvote")
@RequiredArgsConstructor
@Tag(name = "Upvote API", description = "좋아요 기능")
public class UpvoteController {

    private final UpvoteService upvoteService;

    @PostMapping("/{storyId}")
    @Operation(summary = "좋아요 등록/삭제", description = "좋아요 등록/삭제 API")
    public ResponseEntity<String> upvote(@PathVariable(name = "storyId") long storyId,
                                         @MemberInfo MemberInfoDto memberInfoDto) {

        upvoteService.toggleUpvote(storyId, memberInfoDto);

        return ResponseEntity.ok("ok");
    }

}
