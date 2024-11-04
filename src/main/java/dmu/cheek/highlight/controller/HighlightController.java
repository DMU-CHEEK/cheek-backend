package dmu.cheek.highlight.controller;

import dmu.cheek.global.resolver.memberInfo.MemberInfo;
import dmu.cheek.global.resolver.memberInfo.MemberInfoDto;
import dmu.cheek.highlight.model.HighlightDto;
import dmu.cheek.highlight.service.HighlightService;
import dmu.cheek.member.service.MemberService;
import dmu.cheek.s3.model.S3Dto;
import dmu.cheek.s3.service.S3Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/highlight")
@RequiredArgsConstructor
@Tag(name = "Highlight API", description = "하이라이트 기능")
public class HighlightController {

    private final HighlightService highlightService;
    private final S3Service s3Service;

    @PostMapping("")
    @PreAuthorize("hasRole('MENTOR')")
    @Operation(summary = "하이라이트 등록", description = "하이라이트 등록 API")
    public ResponseEntity<String> register(@RequestPart(name = "highlightDto") HighlightDto.Request highlightDto,
                                           @RequestPart(name = "thumbnailPicture", required = false) MultipartFile thumbnailPicture,
                                           @MemberInfo MemberInfoDto memberInfoDto) {
        if (thumbnailPicture != null) {
            S3Dto s3Dto = s3Service.saveFile(thumbnailPicture);
            highlightDto.setThumbnailPicture(s3Dto.getStoreFileName());
        }

        highlightService.register(highlightDto, memberInfoDto);

        return ResponseEntity.ok("ok");
    }

    @DeleteMapping("/{highlightId}")
    @PreAuthorize("hasRole('MENTOR')")
    @Operation(summary = "하이라이트 삭제", description = "하이하이트 삭제 API")
    public ResponseEntity<String> delete(@PathVariable(name = "highlightId") long highlightId) {
        highlightService.delete(highlightId);

        return ResponseEntity.ok("ok");
    }

    @GetMapping("/member/{memberId}")
    @Operation(summary = "하이라이트 리스트 조회", description = "특정 회원의 하이라이트 리스트 조회 API(썸네일만 노출)")
    public ResponseEntity<List> searchByMember(@PathVariable(name = "memberId") long memberId) {

        List<HighlightDto> highlightList = highlightService.searchByMember(memberId);

        return ResponseEntity.ok(highlightList);
    }

    @GetMapping("/{highlightId}")
    @Operation(summary = "하이라이트 단건 조회", description = "하이라이트 단건 조회 API")
    public ResponseEntity<HighlightDto.Response> search(@PathVariable(name = "highlightId") long highlightId) {

        HighlightDto.Response storyList = highlightService.search(highlightId);

        return ResponseEntity.ok(storyList);
    }

    @PatchMapping("/{highlightId}")
    @PreAuthorize("hasRole('MENTOR')")
    @Operation(summary = "하이라이트 수정", description = "하이라이트 스토리 리스트 수정 API")
    public ResponseEntity<String> update(@PathVariable(name = "highlightId") long highlightId,
                                         @RequestPart(name = "highlightDto") HighlightDto.Request highlightDto,
                                         @RequestPart(name = "thumbnailPicture", required = false) MultipartFile thumbnailPicture) {
        if (thumbnailPicture != null) {
            S3Dto s3Dto = s3Service.saveFile(thumbnailPicture);
            highlightDto.setThumbnailPicture(s3Dto.getStoreFileName());
        }
        highlightService.updateHighlightStoryList(highlightId, highlightDto);

        return ResponseEntity.ok("ok");
    }
}
