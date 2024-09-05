package dmu.cheek.folder.controller;

import dmu.cheek.folder.model.FolderDto;
import dmu.cheek.folder.service.FolderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/folder")
public class FolderController {

    private final FolderService folderService;

    @GetMapping()
    public ResponseEntity<List> search(@RequestParam(name = "memberId") long memberId) {
        List<FolderDto.Response> folderList = folderService.findDtoByMember(memberId);

        return ResponseEntity.ok(folderList);
    }
}
