package dmu.cheek.block.service;

import dmu.cheek.block.model.Block;
import dmu.cheek.block.model.BlockDto;
import dmu.cheek.block.repository.BlockRepository;
import dmu.cheek.global.error.ErrorCode;
import dmu.cheek.global.error.exception.BusinessException;
import dmu.cheek.member.model.Member;
import dmu.cheek.member.service.MemberService;
import dmu.cheek.s3.service.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class BlockService {

    private final BlockRepository blockRepository;
    private final MemberService memberService;
    private final S3Service s3Service;

    @Transactional
    public void register(long targetMemberId, long fromMemberId) {
        Member toMember = memberService.findById(targetMemberId);
        Member fromMember = memberService.findById(fromMemberId);

        Block block = Block.builder()
                .toMember(toMember)
                .fromMember(fromMember)
                .build();

        blockRepository.save(block);

        log.info("register block: {}", block.getBlockId());
    }

    @Transactional
    public void delete(long blockId) {
        Block block = blockRepository.findById(blockId)
                .orElseThrow(() -> new BusinessException(ErrorCode.BLOCK_NOT_FOUND));

        blockRepository.delete(block);

        log.info("delete block: {}", block.getBlockId());
    }

    public List<BlockDto.Response> getBlockList(long memberId) {
        List<BlockDto.Response> blockList = blockRepository.findByFromMemberId(memberId)
                .stream()
                .map(block -> BlockDto.Response.builder()
                        .blockId(block.getBlockId())
                        .memberDto(
                                BlockDto.MemberDto.builder()
                                        .memberId(block.getToMember().getMemberId())
                                        .profilePicture(s3Service.getResourceUrl(block.getToMember().getProfilePicture()))
                                        .nickname(block.getToMember().getNickname())
                                        .information(block.getToMember().getInformation())
                                        .build()
                        )
                        .build()
                ).toList();

        log.info("get block list, memberId: {}", memberId);

        return blockList;
    }
}
