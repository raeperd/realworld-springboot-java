package io.github.raeperd.realworld.domain.article;

import io.github.raeperd.realworld.domain.user.User;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.Instant;

import static javax.persistence.GenerationType.AUTO;

@Table(name = "articles")
@EntityListeners(AuditingEntityListener.class)
@Entity
class Article {

    @GeneratedValue(strategy = AUTO)
    @Id
    private Long id;

    @JoinColumn(name = "author_id", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private User author;

    @Embedded
    private ArticleTitle title;

    @Embedded
    private ArticleContents contents;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updateAt;

    protected Article() {
    }
}
