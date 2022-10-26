package com.fastcampus.projectboard.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@ToString
@Table(indexes = {
        @Index(columnList = "content"),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy")
})
//@EntityListeners(AuditingEntityListener.class)
@Entity
public class ArticleComment extends AuditingFields{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter             @ManyToOne(optional = false) private Article article; // N 대 1, optional false 은 이것은 필수값이다라는 의미.
    @Setter             @Column(nullable = false, length = 500) private String content;

//    @CreatedDate        @Column(nullable = false) private LocalDateTime createdAt; // 최초 insert 할 때 생성될 때마다 생성된 시간과 생성한 사람을 실시간으로 추가시켜주겠다는 의미.
//    @CreatedBy          @Column(nullable = false, length = 100) private String createdBy;
//    @LastModifiedDate   @Column(nullable = false) private LocalDateTime modifiedAt; // update 할때마다 수정한 시간과 사람을 실시간으로 수정해주겠다는 의미
//    @LastModifiedBy     @Column(nullable = false, length = 100) private String modifiedBy;

    protected ArticleComment() {
    }

    private ArticleComment(Article article, String content) {
        this.article = article;
        this.content = content;
    }

    public static ArticleComment of(Article article, String content) {
        return new ArticleComment(article, content);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ArticleComment that)) return false;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
