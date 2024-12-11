package dmu.cheek.block.controller;

import dmu.cheek.block.service.BlockService;
import dmu.cheek.global.resolver.memberInfo.MemberInfo;
import dmu.cheek.global.resolver.memberInfo.MemberInfoDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/block")
@RequiredArgsConstructor
@Tag(name = "Block API", description = "차단 기능")
public class BlockController {

    private final BlockService blockService;

    @PostMapping("/{targetMemberId}")
    public ResponseEntity<String> register(@PathVariable(name = "targetMemberId") long targetMemberId,
                                           @MemberInfo MemberInfoDto memberInfoDto) {
        blockService.register(targetMemberId, memberInfoDto.getMemberId());

        return ResponseEntity.ok("ok");
    }
}
