package dmu.cheek.api.member.service;

import dmu.cheek.api.kakao.model.KakaoLoginResponseDto;
import dmu.cheek.api.member.model.Member;
import dmu.cheek.api.member.model.ProfileDto;
import dmu.cheek.api.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    public boolean isExistMember(String email) {
        return memberRepository.findByEmail(email) == null ? false : true;
    }


    @Transactional
    public void register(KakaoLoginResponseDto kakaoLoginResponseDto) {
        Member member = Member.withEmail()
                .email(kakaoLoginResponseDto.getKakaoAccount().getEmail())
                .build();

        memberRepository.save(member);

        log.info("save member: {}", member.getEmail());
    }

    @Transactional
    public void setProfile(ProfileDto profileDto) {
        Member member = findByEmail(profileDto.getEmail());

        member.setProfile(profileDto.getNickname(), profileDto.getInformation(), profileDto.getProfilePicture(), profileDto.getRole());

        log.info("set profile: {}", profileDto.getEmail());
    }

    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email).orElseThrow(RuntimeException::new); //TODO: exception
    }

}