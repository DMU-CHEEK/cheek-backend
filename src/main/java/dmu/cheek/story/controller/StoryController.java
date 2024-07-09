package dmu.cheek.story.controller;

import dmu.cheek.story.model.StoryDto;
import dmu.cheek.story.service.StoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/story")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Story API", description = "스토리 기능")
public class StoryController {

    private final StoryService storyService;

    @PostMapping()
    @Operation(summary = "스토리 등록", description = "스토리 등록 API")
    public ResponseEntity<String> register(@RequestPart(value = "storyPicture") MultipartFile storyPicture,
                                           @RequestPart(value = "storyDto") StoryDto.Request storyDto) {
        storyService.register(storyPicture, storyDto);

        return ResponseEntity.ok("ok");
    }

    @DeleteMapping("/{storyId}")
    @Operation(summary = "스토리 삭제", description = "스토리 삭제 API")
    public ResponseEntity<String> delete(@PathVariable(name = "storyId") long storyId) {
        storyService.delete(storyId);

        return ResponseEntity.ok("ok");
    }

    @GetMapping("/member/{memberId}")
    @Operation(summary = "특정 유저의 스토리 리스트 조회", description = "특정 유저의 스토리 리스트 조회 API")
    public ResponseEntity<List> searchByMember(@PathVariable(name = "memberId") long memberId) {
        List<StoryDto.Response> storyList = storyService.searchByMember(memberId);

        return ResponseEntity.ok(storyList);
    }

    @GetMapping("/{storyId}")
    @Operation(summary = "스토리 조회", description = "스토리 단건 조회 API")
    public ResponseEntity<StoryDto.Response> search(@PathVariable(name = "storyId") long storyId) {
        StoryDto.Response storyDto = storyService.search(storyId);

        return ResponseEntity.ok(storyDto);
    }
}
