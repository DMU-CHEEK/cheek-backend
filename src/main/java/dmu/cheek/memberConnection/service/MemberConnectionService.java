package dmu.cheek.memberConnection.service;

import dmu.cheek.global.error.ErrorCode;
import dmu.cheek.global.error.exception.BusinessException;
import dmu.cheek.member.model.Member;
import dmu.cheek.member.service.MemberService;
import dmu.cheek.memberConnection.model.MemberConnection;
import dmu.cheek.memberConnection.model.MemberConnectionDto;
import dmu.cheek.memberConnection.repository.MemberConnectionRepository;
import dmu.cheek.s3.service.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class MemberConnectionService {

    private final MemberConnectionRepository memberConnectionRepository;
    private final MemberService memberService;
    private final S3Service s3Service;

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
        MemberConnection memberConnection = memberConnectionRepository.findByToMemberAndFromMember(memberConnectionDto.getToMemberId(), memberConnectionDto.getFromMemberId())
                .orElseThrow(() -> new BusinessException(ErrorCode.CONNECTION_NOT_FOUND));

        memberConnectionRepository.delete(memberConnection);

        log.info("member {} unfollowed member {}", memberConnectionDto.getFromMemberId(), memberConnectionDto.getToMemberId());
    }

    public List<MemberConnectionDto.Response> getFollowerList(long targetMemberId, long loginMemberId) {
        List<MemberConnection> targetMemberConnectionList = memberConnectionRepository.findByToMember(targetMemberId);
        Set<Long> loginMemberConnectionList = memberConnectionRepository.findByFromMember(loginMemberId)
                .stream().map(memberConnection -> memberConnection.getToMember().getMemberId()).collect(Collectors.toSet());

        log.info("get member {}'s follower list", targetMemberId);

        return targetMemberConnectionList.stream()
                .map(memberConnection -> MemberConnectionDto.Response
                        .builder()
                        .memberId(memberConnection.getFromMember().getMemberId())
                        .profilePicture(s3Service.getResourceUrl(memberConnection.getFromMember().getProfilePicture()))
                        .isFollowing(loginMemberConnectionList.contains(memberConnection.getFromMember().getMemberId()))
                        .build())
                .toList();
    }

    public List<MemberConnectionDto.Response> getFollowingList(long targetMemberId, long loginMemberId) {
        List<MemberConnection> targetMemberConnectionList = memberConnectionRepository.findByFromMember(targetMemberId);
        Set<Long> loginMemberConnectionList = memberConnectionRepository.findByToMember(loginMemberId)
                .stream().map(memberConnection -> memberConnection.getFromMember().getMemberId()).collect(Collectors.toSet());

        log.info("get member {}'s following list, memberId", targetMemberId);

        return targetMemberConnectionList.stream()
                .map(memberConnection -> MemberConnectionDto.Response
                        .builder()
                        .memberId(memberConnection.getToMember().getMemberId())
                        .profilePicture(s3Service.getResourceUrl(memberConnection.getToMember().getProfilePicture()))
                        .isFollowing(loginMemberConnectionList.contains(memberConnection.getToMember().getMemberId()))
                        .build()
                ).toList();
    }
}
