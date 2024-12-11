package dmu.cheek.block.service;

import dmu.cheek.block.model.Block;
import dmu.cheek.block.repository.BlockRepository;
import dmu.cheek.member.model.Member;
import dmu.cheek.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class BlockService {

    private final BlockRepository blockRepository;
    private final MemberService memberService;

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
}
