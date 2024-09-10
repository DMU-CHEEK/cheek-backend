package dmu.cheek.memberConnection.service;

import dmu.cheek.global.error.ErrorCode;
import dmu.cheek.global.error.exception.BusinessException;
import dmu.cheek.member.model.Member;
import dmu.cheek.member.service.MemberService;
import dmu.cheek.memberConnection.model.MemberConnection;
import dmu.cheek.memberConnection.model.MemberConnectionDto;
import dmu.cheek.memberConnection.repository.MemberConnectionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class MemberConnectionService {

    private final MemberConnectionRepository memberConnectionRepository;
    private final MemberService memberService;

    @Transactional
    public void register(MemberConnectionDto.Request memberConnectionDto) {
        Member fromMember = memberService.findById(memberConnectionDto.getFromMemberId()); //요청한 회원
        Member toMember = memberService.findById(memberConnectionDto.getToMemberId()); //요청받은 회원

        memberConnectionRepository.save(
                MemberConnection.withoutPrimaryKey()
                        .toMember(toMember)
                        .fromMember(fromMember)
                        .build()
        );

        log.info("member {} is following member {}", fromMember.getMemberId(), toMember.getMemberId());
    }

    @Transactional
    public void delete(MemberConnectionDto.Request memberConnectionDto) {
        MemberConnection memberConnection = memberConnectionRepository.findByToMemberAndfromMember(memberConnectionDto.getToMemberId(), memberConnectionDto.getFromMemberId())
                .orElseThrow(() -> new BusinessException(ErrorCode.CONNECTION_NOT_FOUND));

        memberConnectionRepository.delete(memberConnection);

        log.info("Member {} unfollowed Member {}", memberConnectionDto.getFromMemberId(), memberConnectionDto.getToMemberId());
    }
}
