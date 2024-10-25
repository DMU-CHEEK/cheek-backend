package dmu.cheek.story.controller;

import dmu.cheek.global.resolver.memberInfo.MemberInfo;
import dmu.cheek.global.resolver.memberInfo.MemberInfoDto;
import dmu.cheek.member.service.MemberService;
import dmu.cheek.story.model.StoryDto;
import dmu.cheek.story.service.StoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private final MemberService memberService;

    @PostMapping()
    @PreAuthorize("hasRole('MENTOR')")
    @Operation(summary = "스토리 등록", description = "스토리 등록 API")
    public ResponseEntity<String> register(@RequestPart(value = "storyPicture") MultipartFile storyPicture,
                                           @RequestPart(value = "storyDto") StoryDto.Request storyDto,
                                           @MemberInfo MemberInfoDto memberInfoDto) {
        storyService.register(storyPicture, storyDto, memberInfoDto);

        return ResponseEntity.ok("ok");
    }

    @DeleteMapping()
    @PreAuthorize("hasRole('MENTOR')")
    @Operation(summary = "스토리 삭제", description = "스토리 삭제 API")
    public ResponseEntity<String> delete(@RequestBody StoryDto.Delete storyDto) {
        storyService.delete(storyDto);

        return ResponseEntity.ok("ok");
    }

    @GetMapping("/member/{targetMemberId}")
    @Operation(summary = "스토리 리스트 조회", description = "특정 유저의 스토리 리스트 조회 API")
    public ResponseEntity<List> searchByMember(@PathVariable(name = "targetMemberId") long targetMemberId,
                                               @MemberInfo MemberInfoDto memberInfoDto) {
        List<StoryDto.ResponseList> storyList = storyService.searchListByMember(memberInfoDto, targetMemberId);

        return ResponseEntity.ok(storyList);
    }

    @GetMapping("/{storyId}")
    @Operation(summary = "스토리 조회", description = "특정 유저의 스토리 단건 조회 API")
    public ResponseEntity<StoryDto.ResponseOne> search(@PathVariable(name = "storyId") long storyId,
                                                       @MemberInfo MemberInfoDto memberInfoDto) {
        StoryDto.ResponseOne storyDto = storyService.searchByMember(storyId, memberInfoDto);

        return ResponseEntity.ok(storyDto);
    }

    @GetMapping("/{questionId}/answered-stories")
    @Operation(summary = "질문에 답변된 스토리 모아보기", description = "질문에 답변된 스토리 모아보기 API")
    public ResponseEntity<List> getAnsweredStoryList(@PathVariable(name = "questionId") long questionId,
                                                     @MemberInfo MemberInfoDto memberInfoDto) {
        List<StoryDto.AnsweredList> answeredStoryList = storyService.getAnsweredStoryList(questionId, memberInfoDto.getMemberId());

        return ResponseEntity.ok(answeredStoryList);
    }
}
