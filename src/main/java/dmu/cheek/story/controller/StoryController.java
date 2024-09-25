package dmu.cheek.story.controller;

import dmu.cheek.global.error.ErrorCode;
import dmu.cheek.global.error.exception.BusinessException;
import dmu.cheek.member.model.Role;
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

    @PostMapping("/mentor")
    @Operation(summary = "스토리 등록", description = "스토리 등록 API")
    public ResponseEntity<String> register(@RequestPart(value = "storyPicture") MultipartFile storyPicture,
                                           @RequestPart(value = "storyDto") StoryDto.Request storyDto) {
        //TODO: refactor to create a token
        if (memberService.checkRole(storyDto.getMemberId(), Role.MENTOR))
            storyService.register(storyPicture, storyDto);
        else new BusinessException(ErrorCode.ACCESS_DENIED);

        return ResponseEntity.ok("ok");
    }

    @DeleteMapping("/mentor/{storyId}")
    @Operation(summary = "스토리 삭제", description = "스토리 삭제 API")
    public ResponseEntity<String> delete(@PathVariable(name = "storyId") long storyId) {
        //TODO: refactor to create a token
        if (memberService.checkRole(storyService.findById(storyId).getMember().getMemberId(), Role.MENTOR))
            storyService.delete(storyId);
        else new BusinessException(ErrorCode.ACCESS_DENIED);

        return ResponseEntity.ok("ok");
    }

    @GetMapping("/member/{targetMemberId}")
    @Operation(summary = "스토리 리스트 조회", description = "특정 유저의 스토리 리스트 조회 API")
    public ResponseEntity<List> searchByMember(@PathVariable(name = "targetMemberId") long targetMemberId,
                                               @RequestParam(name = "loginMemberId") long loginMemberId) {

        //loginMemberId: 조회하는 유저, targetMemberId: 스토리를 조회할 대상 유저
        List<StoryDto.Response> storyList = storyService.searchListByMember(loginMemberId, targetMemberId);

        return ResponseEntity.ok(storyList);
    }

    @GetMapping("/{storyId}")
    @Operation(summary = "스토리 조회", description = "특정 유저의 스토리 단건 조회 API")
    public ResponseEntity<StoryDto.Response> search(@PathVariable(name = "storyId") long storyId,
                                                    @RequestParam(name = "loginMemberId") long loginMemberId) {
        StoryDto.Response storyDto = storyService.searchByMember(storyId, loginMemberId);

        return ResponseEntity.ok(storyDto);
    }
}
