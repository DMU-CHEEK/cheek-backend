package dmu.cheek.api.member.controller;

import dmu.cheek.api.member.model.ProfileDto;
import dmu.cheek.api.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/profile")
    public ResponseEntity<String> setProfile(@RequestBody ProfileDto profileDto) {

        memberService.setProfile(profileDto);

        return ResponseEntity.ok("ok");
    }
}
