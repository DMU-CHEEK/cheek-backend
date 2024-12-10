package dmu.cheek.report.service;

import dmu.cheek.global.resolver.memberInfo.MemberInfoDto;
import dmu.cheek.member.model.Member;
import dmu.cheek.member.service.MemberService;
import dmu.cheek.report.model.Report;
import dmu.cheek.report.model.ReportDto;
import dmu.cheek.report.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportService {

    private final ReportRepository reportRepository;
    private final MemberService memberService;

    @Transactional
    public void report(ReportDto.Request reportDto, MemberInfoDto memberInfoDto) {
        Member toMember = memberService.findById(reportDto.getToMemberId());
        Member fromMember = memberService.findById(memberInfoDto.getMemberId());

        Report report = Report.builder()
                .toMember(toMember)
                .fromMember(fromMember)
                .title(reportDto.getTitle())
                .content(reportDto.getContent())
                .category(reportDto.getCategory())
                .categoryId(reportDto.getCategoryId())
                .build();

        reportRepository.save(report);

        log.info("register report: {}", report.getReportId());

    }
}
