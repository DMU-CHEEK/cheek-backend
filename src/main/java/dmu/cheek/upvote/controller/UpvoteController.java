package dmu.cheek.upvote.controller;

import dmu.cheek.upvote.model.UpvoteDto;
import dmu.cheek.upvote.service.UpvoteService;
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
public class UpvoteController {

    private final UpvoteService upvoteService;

    @PostMapping()
    public ResponseEntity<String> upvote(@RequestBody UpvoteDto.Request upvoteDto) {

        upvoteService.toggleUpvote(upvoteDto);

        return ResponseEntity.ok("ok");
    }

}
