package dmu.cheek.report.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReportDto {

    private long reportId;
    private Category category;
    private String title;
    private String content;
    private long categoryId;

    @Getter @Builder
    public static class Request {
        private Category category;
        private long categoryId;
        private long toMemberId;
        private String title;
        private String content;
    }

}
