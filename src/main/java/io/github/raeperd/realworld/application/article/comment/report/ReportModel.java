package io.github.raeperd.realworld.application.article.comment.report;

import io.github.raeperd.realworld.application.article.comment.CommentModel.CommentModelNested;
import io.github.raeperd.realworld.application.user.ProfileModel.ProfileModelNested;
import io.github.raeperd.realworld.domain.article.comment.reports.Report;
import lombok.Value;

@Value
public class ReportModel {

    ReportModelNested denounce;

    static ReportModel fromReport(Report report) {
        return new ReportModel(ReportModelNested.fromReport(report));
    }

    @Value
    static class ReportModelNested {
        long id;
        String body;
        String articleTitle;
        CommentModelNested comment;
        ProfileModelNested author;

        static ReportModelNested fromReport(Report report) {
            return new ReportModelNested(report.getId(),
                    report.getBody(),
                    report.getArticleTitle(),
                    CommentModelNested.fromComment(report.getReported()),
                    ProfileModelNested.fromProfile(report.getReporter().getProfile()));
        }
    }
}
