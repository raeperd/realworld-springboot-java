package io.github.raeperd.realworld.domain.article.comment.reports;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report,Long> {
    @Query(value = "select * from comment_reports cr where cr.article_title in (:titles)", nativeQuery = true)
    List<Report> findAllByArticleTitle(List<String> titles);
}
