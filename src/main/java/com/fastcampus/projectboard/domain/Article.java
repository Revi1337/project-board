package com.fastcampus.projectboard.domain;

import lombok.EqualsAndHashCode;
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
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@ToString
@Table(indexes = {
        @Index(columnList = "title"),
        @Index(columnList = "hashtag"),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy")
})
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // notnull
    @Setter @Column(nullable = false) private String title; // Setter 를 class 레벨에 걸지 않고 특정 필드에 거는 이유는 사용자가 특정 필드에 접근하여 세팅하지 못하게끔 막고싶어서 그렇다.
    @Setter @Column(nullable = false, length = 10000) private String content;

    // null 허용
    @Setter private String hashtag;

    @ToString.Exclude // 퍼포먼스 이슈로 인한 toString 제외.
    @OrderBy(value = "id")
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)    // 이것은 article 테이블로 부터 온것이라다는 것을 명시
    private final Set<ArticleComment> articleComments = new LinkedHashSet<>();


    // not null
    @CreatedDate @Column(nullable = false) private LocalDateTime createdAt; // 최초 insert 할 때 생성될 때마다 생성된 시간과 생성한 사람을 실시간으로 추가시켜주겠다는 의미.
    @CreatedBy @Column(nullable = false, length = 100) private String createdBy;
    @LastModifiedDate @Column(nullable = false) private LocalDateTime modifiedAt; // update 할때마다 수정한 시간과 사람을 실시간으로 수정해주겠다는 의미
    @LastModifiedBy @Column(nullable = false, length = 100) private String modifiedBy;


    // 모든 JPA entity 들은 Hibernate 구현체를 사용하는 경우일때 기본 생성자를 갖고 있어야 한다.
    // 여기서는 평소에는 open 하지 않을 것이기 때문에 protected 로 할 것임. (외부에서 new 로 생성하지 못하게 함)
    protected Article() {}

    // id, created.. modified 는 자동으로 생성해줄 것이기 때문에 이를 제외하고 나머지는 직접 설정해주겠다는 의미임.
    // 또한, 외부에서 직접 생성하지 못하게 private 로 막아둘 것이고, factory 메서드를 통해 객체를 만들 수 있게 만들 것임.
    // factory 메서드는 domain Article 을 생성하고자 할 때 어떤 값을 필요로 한다는 가이드를 제공하는 것을 말함.
    private Article(String title, String content, String hashtag) {
        this.title = title;
        this.content = content;
        this.hashtag = hashtag;
    }

    // 이부분이 factory 메서드를 말함.
    // 의도를 전달하는 것임. 도메인 Article 을 생성하고자할때 필요한 요구사항을 안내해주는 역할을 함.
    public static Article of(String title, String content, String hashtag) {
        return new Article(title, content, hashtag);
    }


    // 만약 이것을 리스트같이 콜렉션에서 사용한다고 하면, 중복요소를 제거 혹은 동등 비교를 해야함.
    // 이를 위해 Lombok 의 @EqualsAndHashCode 어노테이션으로 해결할 수 있지만, Entity 애서 만큼은 독특하게 EqualsAndHashCode 를 만들어야 함.
    // 여기서 꿀팁은 alt + insert 로 EqualsAndHashCode 를 오버라이딩 할 떄, null 을 체크하면 Object 랑 비교하여 null 까지 대비해주고
    // not-null 이면 해당 객체와 비교한다.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Article article)) return false;
        return id != null && id.equals(article.id);
    }
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
