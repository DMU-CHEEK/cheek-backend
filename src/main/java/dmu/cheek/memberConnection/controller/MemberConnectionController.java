package dmu.cheek.memberConnection.controller;


import dmu.cheek.global.resolver.memberInfo.MemberInfo;
import dmu.cheek.global.resolver.memberInfo.MemberInfoDto;
import dmu.cheek.memberConnection.model.MemberConnectionDto;
import dmu.cheek.memberConnection.service.MemberConnectionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member-connection")
@Tag(name = "Member-Connection API", description = "팔로우 기능")
public class MemberConnectionController {

    private final MemberConnectionService memberConnectionService;

    @PostMapping("/{toMemberId}")
    @Operation(summary = "팔로우 등록", description = "팔로우 등록 API")
    public ResponseEntity<String> register(@PathVariable(name = "toMemberId") long toMemberId,
                                           @MemberInfo MemberInfoDto memberInfoDto) {
        memberConnectionService.register(toMemberId, memberInfoDto);

        return ResponseEntity.ok("ok");
    }

    @DeleteMapping("/{toMemberId}")
    @Operation(summary = "팔로우 취소", description = "팔로우 취소 API")
    public ResponseEntity<String> delete(@PathVariable(name = "toMemberId") long toMemberId,
                                         @MemberInfo MemberInfoDto memberInfoDto) {
        memberConnectionService.delete(toMemberId, memberInfoDto);

        return ResponseEntity.ok("ok");
    }

    @GetMapping("/follower/{targetMemberId}")
    @Operation(summary = "팔로워 목록 조회", description = "본인을 팔로우한 회원 목록 조회 API")
    public ResponseEntity<List> getFollowerList(@PathVariable(name = "targetMemberId") long targetMemberId,
                                                @MemberInfo MemberInfoDto memberInfoDto) {
        List<MemberConnectionDto.Response> followerList = memberConnectionService.getFollowerList(targetMemberId, memberInfoDto);

        return ResponseEntity.ok(followerList);
    }

    @GetMapping("/following/{targetMemberId}")
    @Operation(summary = "팔로잉 목록 조회", description = "본인이 팔로우한 회원 목록 조회 API")
    public ResponseEntity<List> getFollowingList(@PathVariable(name = "targetMemberId") long targetMemberId,
                                                 @MemberInfo MemberInfoDto memberInfoDto) {
        List<MemberConnectionDto.Response> followingList = memberConnectionService.getFollowingList(targetMemberId, memberInfoDto);

        return ResponseEntity.ok(followingList);
    }
}
