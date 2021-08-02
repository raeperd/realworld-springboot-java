package io.github.raeperd.realworld.domain.article.comment.report;

import io.github.raeperd.realworld.domain.article.comment.Comment;
import io.github.raeperd.realworld.domain.article.comment.reports.Report;
import io.github.raeperd.realworld.domain.user.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ReportTest {

    @Test
    void when_compare_different_report_expect_not_equal(@Mock Comment commentLeft, @Mock User authorLeft,
                                                        @Mock Comment commentRight, @Mock User authorRight) {
        Report reportLeft = createReport(commentLeft,authorLeft,"articleTitle", "body");
        Report reportRight = createReport(commentRight,authorRight,"articleTitle", "body");
        assertThat(reportLeft).isNotEqualTo(reportRight)
                .extracting(Report::hashCode)
                .isNotEqualTo(reportRight.hashCode());
    }

    @Test
    void when_compare_same_report_expect_equal_and_hashCode(@Mock Comment comment, @Mock User author) {
        var reportLeft = createReport(comment, author, "articleTitle", "body");
        var reportRight = createReport(comment, author, "articleTitle", "body");

        assertThat(reportLeft)
                .isEqualTo(reportRight)
                .hasSameHashCodeAs(reportRight);
    }

    private static Report createReport(Comment comment, User author, String articleTitle, String body) {
        return new Report(comment, author, articleTitle, body);
    }
}
