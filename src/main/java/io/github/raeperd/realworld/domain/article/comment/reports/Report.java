package io.github.raeperd.realworld.domain.article.comment.reports;

import io.github.raeperd.realworld.domain.article.comment.Comment;
import io.github.raeperd.realworld.domain.user.User;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Objects;

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.IDENTITY;

@Table(name = "comment_reports")
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Report {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "comment_id", referencedColumnName = "id", nullable = false)
    private Comment reported;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User reporter;

    private String articleTitle;

    private String body;

    public Report(Comment reported, User reporter, String articleTitle, String body) {
        this.reported = reported;
        this.reporter = reporter;
        this.articleTitle = articleTitle;
        this.body = body;
    }

    public Report() {
    }

    public Long getId() {
        return id;
    }

    public Comment getReported() {
        return reported;
    }

    public User getReporter() {
        return reporter;
    }

    public String getBody() {
        return body;
    }

    public String getArticleTitle() {
        return articleTitle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        var report = (Report) o;
        return reported.equals(report.reported) && reporter.equals(report.reporter) && body.equals(report.body);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reported, reporter, body);
    }
}
