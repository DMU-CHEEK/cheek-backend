package dmu.cheek.story.controller;

import dmu.cheek.story.model.StoryDto;
import dmu.cheek.story.service.StoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/story")
@Slf4j
@RequiredArgsConstructor
public class StoryController {

    private final StoryService storyService;

    @PostMapping()
    public ResponseEntity<String> register(@RequestPart(value = "storyPicture") MultipartFile storyPicture,
                                           @RequestPart(value = "storyDto") StoryDto.Request storyDto) {
        storyService.register(storyPicture, storyDto);

        return ResponseEntity.ok("ok");
    }
}
