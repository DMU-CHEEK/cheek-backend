package dmu.cheek.memberConnection.service;

import dmu.cheek.global.error.ErrorCode;
import dmu.cheek.global.error.exception.BusinessException;
import dmu.cheek.global.resolver.memberInfo.MemberInfoDto;
import dmu.cheek.member.model.Member;
import dmu.cheek.member.service.MemberService;
import dmu.cheek.memberConnection.model.MemberConnection;
import dmu.cheek.memberConnection.model.MemberConnectionDto;
import dmu.cheek.memberConnection.repository.MemberConnectionRepository;
import dmu.cheek.noti.model.Notification;
import dmu.cheek.noti.model.Type;
import dmu.cheek.noti.service.NotificationService;
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
    private final NotificationService notificationService;

    @Transactional
    public void register(long toMemberId, MemberInfoDto memberInfoDto) {
        Member fromMember = memberService.findById(memberInfoDto.getMemberId()); //요청한 회원
        Member toMember = memberService.findById(toMemberId); //요청받은 회원

        if (memberConnectionRepository.findByToMemberAndFromMember(
                toMemberId,
                memberInfoDto.getMemberId()
        ).isPresent())
            throw new BusinessException(ErrorCode.DUPLICATED_CONNECTION);

        MemberConnection memberConnection = memberConnectionRepository.save(
                MemberConnection.withoutPrimaryKey()
                        .toMember(toMember)
                        .fromMember(fromMember)
                        .build()
        );

        log.info("member {} is following member {}", fromMember.getMemberId(), toMember.getMemberId());

        //send notification
        String notiBody = String.format("%s님이 나를 팔로우했어요.", fromMember.getNickname());

        notificationService.register(
                Notification.withoutPrimaryKey()
                        .type(Type.from("MEMBER_CONNECTION"))
                        .typeId(memberConnection.getMemberConnectionId())
                        .fromMember(fromMember)
                        .toMember(toMember)
                        .body(notiBody)
                        .build()
        );
    }

    @Transactional
    public void delete(long toMemberId, MemberInfoDto memberInfoDto) {
        MemberConnection memberConnection = memberConnectionRepository.findByToMemberAndFromMember(toMemberId, memberInfoDto.getMemberId())
                .orElseThrow(() -> new BusinessException(ErrorCode.CONNECTION_NOT_FOUND));

        memberConnectionRepository.delete(memberConnection);

        log.info("member {} unfollowed member {}", memberInfoDto.getMemberId(), toMemberId);
    }

    public List<MemberConnectionDto.Response> getFollowerList(long targetMemberId, MemberInfoDto memberInfoDto) {
        List<MemberConnection> targetMemberConnectionList = memberConnectionRepository.findByToMember(targetMemberId);
        Set<Long> loginMemberConnectionList = memberConnectionRepository.findByFromMember(memberInfoDto.getMemberId())
                .stream().map(memberConnection -> memberConnection.getToMember().getMemberId()).collect(Collectors.toSet());

        log.info("get member {}'s follower list", targetMemberId);

        return targetMemberConnectionList.stream()
                .map(memberConnection -> MemberConnectionDto.Response
                        .builder()
                        .memberId(memberConnection.getFromMember().getMemberId())
                        .profilePicture(s3Service.getResourceUrl(memberConnection.getFromMember().getProfilePicture()))
                        .nickname(memberConnection.getFromMember().getNickname())
                        .information(memberConnection.getFromMember().getInformation())
                        .followerCnt(memberConnection.getFromMember().getToMemberConnectionList().size())
                        .isFollowing(loginMemberConnectionList.contains(memberConnection.getFromMember().getMemberId()))
                        .build())
                .toList();
    }

    public List<MemberConnectionDto.Response> getFollowingList(long targetMemberId, MemberInfoDto memberInfoDto) {
        List<MemberConnection> targetMemberConnectionList = memberConnectionRepository.findByFromMember(targetMemberId);
        Set<Long> loginMemberConnectionList = memberConnectionRepository.findByToMember(memberInfoDto.getMemberId())
                .stream().map(memberConnection -> memberConnection.getFromMember().getMemberId()).collect(Collectors.toSet());

        log.info("get member {}'s following list, memberId", targetMemberId);

        return targetMemberConnectionList.stream()
                .map(memberConnection -> MemberConnectionDto.Response
                        .builder()
                        .memberId(memberConnection.getToMember().getMemberId())
                        .profilePicture(s3Service.getResourceUrl(memberConnection.getToMember().getProfilePicture()))
                        .nickname(memberConnection.getToMember().getNickname())
                        .information(memberConnection.getToMember().getInformation())
                        .followerCnt(memberConnection.getToMember().getToMemberConnectionList().size())
                        .isFollowing(loginMemberConnectionList.contains(memberConnection.getToMember().getMemberId()))
                        .build()
                ).toList();
    }
}
