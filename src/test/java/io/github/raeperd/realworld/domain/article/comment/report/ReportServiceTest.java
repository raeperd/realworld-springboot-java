package io.github.raeperd.realworld.domain.article.comment.report;

import io.github.raeperd.realworld.application.article.comment.report.ReportPostRequestDTO;
import io.github.raeperd.realworld.domain.article.Article;
import io.github.raeperd.realworld.domain.article.comment.Comment;
import io.github.raeperd.realworld.domain.article.comment.CommentService;
import io.github.raeperd.realworld.domain.article.comment.reports.ReportConstants;
import io.github.raeperd.realworld.domain.article.comment.reports.ReportException;
import io.github.raeperd.realworld.domain.article.comment.reports.ReportRepository;
import io.github.raeperd.realworld.domain.article.comment.reports.ReportService;
import io.github.raeperd.realworld.domain.user.User;
import io.github.raeperd.realworld.domain.user.UserFindService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

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

    @BeforeEach
    private void initializeService() {
        reportService = new ReportService(userFindService,commentService,reportRepository);
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

    private static Comment createComment(Article article, User user, String body) {
        return new Comment(article,user,body);
    }
}
