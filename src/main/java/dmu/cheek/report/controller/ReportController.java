package dmu.cheek.report.controller;

import dmu.cheek.global.resolver.memberInfo.MemberInfo;
import dmu.cheek.global.resolver.memberInfo.MemberInfoDto;
import dmu.cheek.report.model.ReportDto;
import dmu.cheek.report.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/report")
@RequiredArgsConstructor
@Tag(name = "Report API", description = "신고 기능")
public class ReportController {

    private final ReportService reportService;

    @PostMapping()
    @Operation(summary = "신고 등록", description = "신고 등록 API")
    public ResponseEntity<String> report(@RequestBody ReportDto.Request reportDto,
                                         @MemberInfo MemberInfoDto memberInfoDto) {

        reportService.report(reportDto, memberInfoDto);

        return ResponseEntity.ok("ok");
    }
}
