package io.github.raeperd.realworld.domain.article.comment.reports;

import io.github.raeperd.realworld.application.article.comment.report.ReportPostRequestDTO;
import io.github.raeperd.realworld.domain.article.comment.Comment;
import io.github.raeperd.realworld.domain.article.comment.CommentService;
import io.github.raeperd.realworld.domain.user.User;
import io.github.raeperd.realworld.domain.user.UserFindService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class ReportService {

    private final UserFindService userFindService;
    private final CommentService commentService;
    private final ReportRepository reportRepository;

    public ReportService(UserFindService userFindService, CommentService commentService, ReportRepository reportRepository) {
        this.userFindService = userFindService;
        this.commentService = commentService;
        this.reportRepository = reportRepository;
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

    private void allowReport(User reporter, Comment reported) {
        if(reporter.equals(reported.getAuthor())){
            throw new ReportException(ReportConstants.USER_NOT_ALLOWED);
        }
    }
}
