package dmu.cheek.memberConnection.controller;


import dmu.cheek.memberConnection.model.MemberConnectionDto;
import dmu.cheek.memberConnection.service.MemberConnectionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member-connection")
@Tag(name = "Member-Connection API", description = "팔로우 기능")
public class MemberConnectionController {

    private final MemberConnectionService memberConnectionService;

    @PostMapping()
    @Operation(summary = "팔로우 등록", description = "팔로우 등록 API")
    public ResponseEntity<String> register(@RequestBody MemberConnectionDto.Request memberConnectionDto) {
        memberConnectionService.register(memberConnectionDto);

        return ResponseEntity.ok("ok");
    }

    @DeleteMapping()
    @Operation(summary = "팔로우 취소", description = "팔로우 취소 API")
    public ResponseEntity<String> delete(@RequestBody MemberConnectionDto.Request memberConnectionDto) {
        memberConnectionService.delete(memberConnectionDto);

        return ResponseEntity.ok("ok");
    }
}
