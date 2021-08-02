package io.github.raeperd.realworld.application.article.comment.report;

import io.github.raeperd.realworld.domain.article.comment.reports.ReportService;
import io.github.raeperd.realworld.infrastructure.jwt.UserJWTPayload;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class ReportRestController {

    private final ReportService reportService;

    public ReportRestController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping("/article/{slug}/comment/{id}/denounce")
    public ReportModel createReportOnComment(@AuthenticationPrincipal UserJWTPayload jwtPayload,
                                             @PathVariable String slug,
                                             @PathVariable Long id,
                                             @Valid @RequestBody ReportPostRequestDTO dto) {
        final var reportAdded = reportService.createReport(jwtPayload.getUserId(),slug,id,dto);
        return ReportModel.fromReport(reportAdded);
    }
}