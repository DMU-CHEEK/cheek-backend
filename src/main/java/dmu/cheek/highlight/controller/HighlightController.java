package dmu.cheek.highlight.controller;

import dmu.cheek.highlight.model.HighlightDto;
import dmu.cheek.highlight.service.HighlightService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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

    @GetMapping("/member/{memberId}")
    @Operation(summary = "하이라이트 리스트 조회", description = "특정 회읜의 하이라이트 리스트 조회 API(썸네일만 노출)")
    public ResponseEntity<List> searchByMember(@PathVariable(value = "memberId") long memberId) {

        List<HighlightDto> highlightList = highlightService.searchByMember(memberId);

        return ResponseEntity.ok(highlightList);
    }

    @GetMapping("/{highlightId}")
    @Operation(summary = "하이라이트 단건 조회", description = "하이라이트 단건 조회 API")
    public ResponseEntity<HighlightDto.Response> search(@PathVariable(value = "highlightId") long highlightId) {

        HighlightDto.Response highlight = highlightService.search(highlightId);

        return ResponseEntity.ok(highlight);
    }
}
