package io.github.raeperd.realworld.application.article.comment.report;

import io.github.raeperd.realworld.domain.article.comment.reports.Report;

import java.util.ArrayList;
import java.util.List;

public class MultipleReportModel {

    private List<MultipleReportData> reports = new ArrayList<>();

    public List<MultipleReportData> getReports() {
        return reports;
    }

    public void setReports(MultipleReportData report) {
        this.reports.add(report);
    }

    public static class MultipleReportData {
        private final String comment;
        private final String article;
        private List<ReducedReportDTO> reports = new ArrayList<>();

        public MultipleReportData(Report report) {
            this.comment = report.getReported().getBody();
            this.article = report.getArticleTitle();
            this.reports.add(new ReducedReportDTO(
                    report.getId(),
                    report.getBody()
            ));
        }

        public String getComment() {
            return comment;
        }

        public String getArticle() {
            return article;
        }

        public List<ReducedReportDTO> getReports() {
            return reports;
        }

        public void setReports(Report report) {
            this.reports.add(new ReducedReportDTO(
                    report.getId(),
                    report.getBody())
            );
        }
    }
}
