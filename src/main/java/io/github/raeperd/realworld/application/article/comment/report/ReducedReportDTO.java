package io.github.raeperd.realworld.application.article.comment.report;

import io.github.raeperd.realworld.domain.article.comment.reports.Report;
import lombok.Value;

@Value
public class ReducedReportDTO {
    Long id;
    String body;

    static ReducedReportDTO fromReport(Report report) {
        return new ReducedReportDTO(report.getId(), report.getBody());
    }
}
