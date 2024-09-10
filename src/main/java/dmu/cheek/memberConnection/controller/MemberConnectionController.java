package dmu.cheek.memberConnection.controller;


import dmu.cheek.memberConnection.model.MemberConnectionDto;
import dmu.cheek.memberConnection.service.MemberConnectionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member-connection")
@Tag(name = "Member-Connection API", description = "팔로우 기능")
public class MemberConnectionController {

    private final MemberConnectionService memberConnectionService;

    @PostMapping()
    @Operation(summary = "팔로우 신청", description = "팔로우 신청 API")
    public ResponseEntity<String> register(@RequestBody MemberConnectionDto.Request memberConnectionDto) {
        memberConnectionService.register(memberConnectionDto);

        return ResponseEntity.ok("ok");
    }
}
