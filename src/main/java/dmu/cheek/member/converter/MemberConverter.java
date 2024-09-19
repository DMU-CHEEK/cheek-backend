package dmu.cheek.member.converter;

import dmu.cheek.member.model.Member;
import dmu.cheek.member.model.MemberDto;
import dmu.cheek.s3.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberConverter {

    private final S3Service s3Service;

    public MemberDto convertToDto(Member member) {
        if (member == null)
            return null;

        return MemberDto.builder()
                .memberId(member.getMemberId())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .description(member.getDescription())
                .information(member.getInformation())
                .profilePicture(s3Service.getResourceUrl(member.getProfilePicture()))
                .role(member.getRole())
                .status(member.getStatus())
                .build();
    }

    public Member convertToEntity(MemberDto memberDto) {
        if (memberDto == null)
            return null;

        return Member.allFields()
                .memberId(memberDto.getMemberId())
                .email(memberDto.getEmail())
                .nickname(memberDto.getNickname())
                .description(memberDto.getDescription())
                .information(memberDto.getInformation())
                .profilePicture(memberDto.getProfilePicture())
                .role(memberDto.getRole())
                .status(memberDto.getStatus())
                .build();
    }
}
