package dmu.cheek.memberConnection.controller;


import dmu.cheek.memberConnection.model.MemberConnectionDto;
import dmu.cheek.memberConnection.service.MemberConnectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member-connection")
public class MemberConnectionController {

    private final MemberConnectionService memberConnectionService;

    @PostMapping()
    public ResponseEntity<String> register(@RequestBody MemberConnectionDto.Request memberConnectionDto) {
        memberConnectionService.register(memberConnectionDto);

        return ResponseEntity.ok("ok");
    }
}
