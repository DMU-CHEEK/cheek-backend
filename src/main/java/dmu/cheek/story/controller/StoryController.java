package dmu.cheek.story.controller;

import dmu.cheek.global.error.ErrorCode;
import dmu.cheek.global.error.exception.BusinessException;
import dmu.cheek.global.resolver.memberInfo.MemberInfo;
import dmu.cheek.global.resolver.memberInfo.MemberInfoDto;
import dmu.cheek.member.constant.Role;
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
                                               @RequestParam(name = "loginMemberId") long loginMemberId) {

        //loginMemberId: 조회하는 유저, targetMemberId: 스토리를 조회할 대상 유저
        List<StoryDto.ResponseList> storyList = storyService.searchListByMember(loginMemberId, targetMemberId);

        return ResponseEntity.ok(storyList);
    }

    @GetMapping("/{storyId}")
    @Operation(summary = "스토리 조회", description = "특정 유저의 스토리 단건 조회 API")
    public ResponseEntity<StoryDto.ResponseOne> search(@PathVariable(name = "storyId") long storyId,
                                                    @RequestParam(name = "loginMemberId") long loginMemberId) {
        StoryDto.ResponseOne storyDto = storyService.searchByMember(storyId, loginMemberId);

        return ResponseEntity.ok(storyDto);
    }

    @GetMapping("/{questionId}/answered-stories")
    @Operation(summary = "질문에 답변된 스토리 모아보기", description = "질문에 답변된 스토리 모아보기 API")
    public ResponseEntity<List> getAnsweredStoryList(@PathVariable(name = "questionId") long questionId,
                                                     @RequestParam(name = "loginMemberId") long loginMemberId) {
        List<StoryDto.ResponseList> answeredStoryList = storyService.getAnsweredStoryList(questionId, loginMemberId);

        return ResponseEntity.ok(answeredStoryList);
    }
}
