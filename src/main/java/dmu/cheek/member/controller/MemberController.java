package dmu.cheek.member.controller;

import dmu.cheek.member.model.ProfileDto;
import dmu.cheek.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping(value = "/profile", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> setProfile(@RequestPart(value = "profileDto") ProfileDto profileDto,
                                             @RequestPart(value = "profilePicture") MultipartFile profilePicture) throws IOException {

        memberService.setProfile(profileDto, profilePicture);

        return ResponseEntity.ok("ok");
    }
}
