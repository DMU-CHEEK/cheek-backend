package dmu.cheek.folder.controller;

import dmu.cheek.folder.model.FolderDto;
import dmu.cheek.folder.service.FolderService;
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

    @GetMapping("/{memberId}")
    @Operation(summary = "폴더 검색", description = "스크랩한 폴더 리스트 검색 API")
    public ResponseEntity<List> search(@PathVariable(name = "memberId") long memberId) {
        List<FolderDto.Response> folderList = folderService.findDtoByMember(memberId);

        return ResponseEntity.ok(folderList);
    }

    @DeleteMapping("/{folderId}")
    @Operation(summary = "폴더 삭제", description = "스크랩한 폴더 삭제 API")
    public ResponseEntity<String> delete(@PathVariable(name = "folderId") long folderId) {
        folderService.delete(folderId);

        return ResponseEntity.ok("ok");
    }
}
