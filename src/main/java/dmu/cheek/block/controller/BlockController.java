package dmu.cheek.block.controller;

import dmu.cheek.block.model.BlockDto;
import dmu.cheek.block.service.BlockService;
import dmu.cheek.global.resolver.memberInfo.MemberInfo;
import dmu.cheek.global.resolver.memberInfo.MemberInfoDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/block")
@RequiredArgsConstructor
@Tag(name = "Block API", description = "차단 기능")
public class BlockController {

    private final BlockService blockService;

    @PostMapping("/{targetMemberId}")
    @Operation(summary = "차단 등록", description = "차단 등록 API")
    public ResponseEntity<String> register(@PathVariable(name = "targetMemberId") long targetMemberId,
                                           @MemberInfo MemberInfoDto memberInfoDto) {
        blockService.register(targetMemberId, memberInfoDto.getMemberId());

        return ResponseEntity.ok("ok");
    }

    @DeleteMapping("/{blockId}")
    @Operation(summary = "차단 해제", description = "차단 해제 API")
    public ResponseEntity<String> delete(@PathVariable(name = "blockId") long blockId) {
        blockService.delete(blockId);

        return ResponseEntity.ok("ok");
    }

    @GetMapping()
    @Operation(summary = "차단 유저 조회", description = "차단 유저 조회 API")
    public ResponseEntity<List> getBlockList(@MemberInfo MemberInfoDto memberInfoDto) {
        List<BlockDto.Response> blockList = blockService.getBlockList(memberInfoDto.getMemberId());

        return ResponseEntity.ok(blockList);
    }
}
