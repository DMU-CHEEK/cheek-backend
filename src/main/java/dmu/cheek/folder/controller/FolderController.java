package dmu.cheek.folder.controller;

import dmu.cheek.folder.model.FolderDto;
import dmu.cheek.folder.service.FolderService;
import dmu.cheek.global.resolver.memberInfo.MemberInfo;
import dmu.cheek.global.resolver.memberInfo.MemberInfoDto;
import dmu.cheek.story.model.StoryDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/folder")
@Tag(name = "Folder API", description = "스크랩한 폴더 기능")
public class FolderController {

    private final FolderService folderService;

    @GetMapping()
    @Operation(summary = "폴더 조회", description = "스크랩한 폴더 리스트 검색 API")
    public ResponseEntity<List> search(@MemberInfo MemberInfoDto memberInfoDto) {
        List<FolderDto.Response> folderList = folderService.findDtoByMember(memberInfoDto);

        return ResponseEntity.ok(folderList);
    }

    @DeleteMapping("/{folderId}")
    @Operation(summary = "폴더 삭제", description = "스크랩한 폴더 삭제 API")
    public ResponseEntity<String> delete(@PathVariable(name = "folderId") long folderId) {
        folderService.delete(folderId);

        return ResponseEntity.ok("ok");
    }

    @GetMapping("/story/{folderId}")
    @Operation(summary = "폴더 별 스크랩 스토리 조회", description = "폴더 별 스크랩 스토리 조회 API")
    public ResponseEntity<List> searchStoryList(@PathVariable(name = "folderId") long folderId) {
        List<StoryDto.Collection> storyList = folderService.search(folderId);

        return ResponseEntity.ok(storyList);
    }
}
