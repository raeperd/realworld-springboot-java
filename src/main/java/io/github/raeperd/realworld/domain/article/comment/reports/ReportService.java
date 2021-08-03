package io.github.raeperd.realworld.domain.article.comment.reports;

import io.github.raeperd.realworld.application.article.comment.report.MultipleReportModel;
import io.github.raeperd.realworld.application.article.comment.report.ReportPostRequestDTO;
import io.github.raeperd.realworld.domain.article.Article;
import io.github.raeperd.realworld.domain.article.ArticleRepository;
import io.github.raeperd.realworld.domain.article.comment.Comment;
import io.github.raeperd.realworld.domain.article.comment.CommentService;
import io.github.raeperd.realworld.domain.user.User;
import io.github.raeperd.realworld.domain.user.UserFindService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportService {

    private final UserFindService userFindService;
    private final CommentService commentService;
    private final ReportRepository reportRepository;
    private final ArticleRepository articleRepository;

    public ReportService(UserFindService userFindService, CommentService commentService, ReportRepository reportRepository, ArticleRepository articleRepository) {
        this.userFindService = userFindService;
        this.commentService = commentService;
        this.reportRepository = reportRepository;
        this.articleRepository = articleRepository;
    }
    @Transactional
    public Report createReport(long user_id, String slug, Long comment_id, ReportPostRequestDTO dto) {
        final User reporter = userFindService.findById(user_id).orElseThrow(() -> new ReportException(ReportConstants.USER_NOT_FOUND));
        final Comment reported = commentService.getCommentById(user_id,slug,comment_id).orElseThrow(() -> new ReportException(ReportConstants.COMMENT_NOT_FOUND));

        allowReport(reporter,reported);

        return reportRepository.save(new Report(reported,
                reporter,
                reported.getArticle().getContents().getTitle().getTitle(),
                dto.getBody())
        );
    }

    public MultipleReportModel getAllReportsToThisUser(long userId) {
        return splitAndReorganizeData(
                reportRepository.findAllByArticleTitle(
                        articleRepository.findAllByAuthor(userId)
                )
        );
    }

    @Transactional
    public void removeReport(long userId, Long id) {
        Report report = reportRepository.findById(id).orElseThrow(() -> new ReportException(ReportConstants.REPORT_NOT_FOUND));
        Article article = articleRepository.findByTitle(report.getArticleTitle()).orElseThrow(() -> new ReportException(ReportConstants.ARTICLE_NOT_FOUND));
        if(article.getAuthor().equals(userFindService.findById(userId).orElseThrow(() -> new ReportException(ReportConstants.USER_NOT_FOUND)))) {
            reportRepository.deleteById(id);
        } else {
            throw new ReportException(ReportConstants.USER_NOT_ALLOWED);
        }
    }

    private void allowReport(User reporter, Comment reported) {
        if(reporter.equals(reported.getAuthor())){
            throw new ReportException(ReportConstants.USER_NOT_ALLOWED);
        }
    }

    private MultipleReportModel splitAndReorganizeData(List<Report> reports) {
        Map<Comment, MultipleReportModel.MultipleReportData> reportList = new HashMap<>();
        reports.forEach(report -> {
            if(!reportList.containsKey(report.getReported())) {
                reportList.put(report.getReported(),new MultipleReportModel.MultipleReportData(report));
            } else {
                reportList.get(report.getReported()).setReports(report);
            }
        });
        MultipleReportModel listOfDenounces = new MultipleReportModel();
        reportList.forEach((comment, multipleReportModel) -> listOfDenounces.setReports(multipleReportModel));

        return listOfDenounces;
    }
}
