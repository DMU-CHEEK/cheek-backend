package dmu.cheek.collection.controller;

import dmu.cheek.collection.model.CollectionDto;
import dmu.cheek.collection.service.CollectionService;
import dmu.cheek.global.resolver.memberInfo.MemberInfo;
import dmu.cheek.global.resolver.memberInfo.MemberInfoDto;
import dmu.cheek.story.model.StoryDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/collection")
@RequiredArgsConstructor
@Tag(name = "Collection API", description = "스크랩 기능")
public class CollectionController {

    private final CollectionService collectionService;

    @PostMapping()
    @Operation(summary = "스크랩 등록", description = "스크랩 스토리 추가 API")
    public ResponseEntity<String> register(@RequestBody CollectionDto.Request collectionDto,
                                           @MemberInfo MemberInfoDto memberInfoDto) {
        collectionService.register(collectionDto, memberInfoDto);

        return ResponseEntity.ok("ok");
    }

    @DeleteMapping("")
    @Operation(summary = "스크랩 삭제", description = "스크랩 스토리 삭제 API")
    public ResponseEntity<String> delete(@RequestBody CollectionDto.Delete collectionDto) {
        collectionService.delete(collectionDto);

        return ResponseEntity.ok("ok");
    }
}
