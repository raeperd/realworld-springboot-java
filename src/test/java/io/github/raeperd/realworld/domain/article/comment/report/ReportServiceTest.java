package io.github.raeperd.realworld.domain.article.comment.report;

import io.github.raeperd.realworld.application.article.comment.report.ReportPostRequestDTO;
import io.github.raeperd.realworld.domain.article.Article;
import io.github.raeperd.realworld.domain.article.ArticleRepository;
import io.github.raeperd.realworld.domain.article.comment.Comment;
import io.github.raeperd.realworld.domain.article.comment.CommentService;
import io.github.raeperd.realworld.domain.article.comment.reports.*;
import io.github.raeperd.realworld.domain.user.User;
import io.github.raeperd.realworld.domain.user.UserFindService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReportServiceTest {

    private ReportService reportService;

    @Mock
    private UserFindService userFindService;
    @Mock
    private CommentService commentService;
    @Mock
    private ReportRepository reportRepository;
    @Mock
    private ArticleRepository articleRepository;

    @BeforeEach
    private void initializeService() {
        reportService = new ReportService(userFindService,commentService,reportRepository,articleRepository);
    }

    @Test
    void when_comment_not_found_return_ReportException_and_comment_not_found_message(@Mock User user, @Mock ReportPostRequestDTO dto) {
        when(userFindService.findById(1L)).thenReturn(Optional.ofNullable(user));
        assertThatThrownBy(() ->
                reportService.createReport(1L, "slug", 2L, dto)
        ).isInstanceOf(ReportException.class).hasMessage(ReportConstants.COMMENT_NOT_FOUND);
    }

    @Test
    void when_user_not_found_return_ReportException_and_user_not_found_message(@Mock ReportPostRequestDTO dto) {
        assertThatThrownBy(() ->
                reportService.createReport(1L, "slug", 2L, dto)
        ).isInstanceOf(ReportException.class).hasMessage(ReportConstants.USER_NOT_FOUND);
    }

    @Test
    void must_ReportException_and_user_not_allowed_message(@Mock User user, @Mock Article article, @Mock ReportPostRequestDTO dto) {
        when(userFindService.findById(1L)).thenReturn(Optional.ofNullable(user));
        when(commentService.getCommentById(1L, "slug", 2L)).thenReturn(Optional.of(createComment(article,user,"content")));
        assertThatThrownBy(() ->
                reportService.createReport(1L, "slug", 2L, dto)
        ).isInstanceOf(ReportException.class).hasMessage(ReportConstants.USER_NOT_ALLOWED);
    }

    @Test
    void number_of_reported_comments_must_match(@Mock List<String> articleTitles, @Mock Comment firstComment, @Mock Comment secondComment, @Mock User user) {
        when(articleRepository.findAllByAuthor(1L)).thenReturn(articleTitles);
        when(reportRepository.findAllByArticleTitle(articleTitles))
                .thenReturn(List.of(
                        createReport(firstComment,user,"first-article","first-report-of-first-comment"),
                        createReport(firstComment,user,"first-article","second-report-of-first-comment"),
                        createReport(secondComment,user,"second-article","first-report-of-second-comment")
                ));
        assertThat(
                reportService.getAllReportsToThisUser(1L).getReports().size()
        ).isEqualTo(2);
    }

    @Test
    void number_of_reports_and_number_of_reported_comments_must_match(@Mock List<String> articleTitles, @Mock Comment firstComment, @Mock User user) {
        when(articleRepository.findAllByAuthor(1L)).thenReturn(articleTitles);
        when(reportRepository.findAllByArticleTitle(articleTitles))
                .thenReturn(List.of(
                        createReport(firstComment,user,"first-article","first-report-of-first-comment"),
                        createReport(firstComment,user,"first-article","second-report-of-first-comment")
                ));
        assertThat(
                reportService.getAllReportsToThisUser(1L).getReports().get(0).getReports().size()
        ).isEqualTo(2);
        assertThat(
                reportService.getAllReportsToThisUser(1L).getReports().size()
        ).isEqualTo(1);
    }

    private static Comment createComment(Article article, User user, String body) {
        return new Comment(article,user,body);
    }

    private static Report createReport(Comment comment, User user, String articleTitle, String body) {
        return new Report(comment,user,articleTitle,body);
    }
}
