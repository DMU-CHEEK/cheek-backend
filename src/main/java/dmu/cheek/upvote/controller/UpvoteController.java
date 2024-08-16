package dmu.cheek.upvote.controller;

import dmu.cheek.upvote.model.UpvoteDto;
import dmu.cheek.upvote.service.UpvoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/upvote")
@RequiredArgsConstructor
@Tag(name = "Upvote API", description = "좋아요 기능")
public class UpvoteController {

    private final UpvoteService upvoteService;

    @PostMapping()
    @Operation(summary = "좋아요 등록/삭제", description = "좋아요 등록/삭제 API")
    public ResponseEntity<String> upvote(@RequestBody UpvoteDto.Request upvoteDto) {

        upvoteService.toggleUpvote(upvoteDto);

        return ResponseEntity.ok("ok");
    }

}
