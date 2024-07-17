package dmu.cheek.highlight.controller;

import dmu.cheek.highlight.model.HighlightDto;
import dmu.cheek.member.service.HighlightService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/highlight")
@RequiredArgsConstructor
@Tag(name = "Highlight API", description = "하이라이트 기능")
public class HighlightController {

    private final HighlightService highlightService;

    @PostMapping()
    @Operation(summary = "하이라이트 등록", description = "하이라이트 등록 API")
    public ResponseEntity<String> register(@RequestPart(value = "thumbnailPicture") MultipartFile thumbnailPicture,
                                           @RequestPart(value = "highlightDto") HighlightDto.Request highlightDto) {

        highlightService.register(thumbnailPicture, highlightDto);

        return ResponseEntity.ok("ok");
    }

    @DeleteMapping("/{highlightId}")
    @Operation(summary = "하이라이트 삭제", description = "하이하이트 삭제 API")
    public ResponseEntity<String> delete(@PathVariable(value = "highlightId") long highlightId) {

        highlightService.delete(highlightId);

        return ResponseEntity.ok("ok");
    }
}
