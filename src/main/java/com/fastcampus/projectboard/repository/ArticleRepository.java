package com.fastcampus.projectboard.repository;

import com.fastcampus.projectboard.domain.Article;
import com.fastcampus.projectboard.domain.QArticle;
import com.fastcampus.projectboard.repository.querydsl.ArticleRepositoryCustom;
import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ArticleRepository extends
        JpaRepository<Article, Long>,
        ArticleRepositoryCustom,
        QuerydslPredicateExecutor<Article>, // 기본적으로 Article 안에있는 모든 Field 에 대한 기본 검색 기능을 추가해줌. (사실 이거 하나만 넣어도 검색 기능은 끝남)
        QuerydslBinderCustomizer<QArticle>  // QueryBindCustomizer 에 들어가는 제네릭은 QClass 넣어주게 되어있음. (이것의 기능은 입맛에 맞게 검색기능을 추가할 수 있음.)
{
    Page<Article> findByTitleContaining(String title, Pageable pageable);
    Page<Article> findByContentContaining(String content, Pageable pageable);
    Page<Article> findByUserAccount_UserIdContaining(String userId, Pageable pageable);
    Page<Article> findByUserAccount_NicknameContaining(String nickname, Pageable pageable);
    Page<Article> findByHashtag(String hashtag, Pageable pageable);


    @Override                                                                       // QueryBindCustomizer 의 customize 메서드를 오버라이딩해야 해당 메서드안에 구현되어있는 내용을 토대로 세부적인 규칙이 재구성됨.
    default void customize(QuerydslBindings bindings, QArticle root) {              // 원래 인터페이스에서는 구현을 넣을 수 없지만 java 8 이후로 가능해짐.
        bindings.excludeUnlistedProperties(true);                                   // 현재 QuerydslPredicateExecutor 기능에 의해서 Article 에 있는 모든 Field 들에 대한 검색이 열려있는데, 이를 선택적인 Field 로 검색을 가능하게 해줌.
        bindings.including(root.title, root.content, root.hashtag, root.createdAt, root.createdBy); // 요놈으로 원하는 필드들을 선택해 줄 수 있음.
        bindings.bind(root.title).first(StringExpression::containsIgnoreCase);      // 검색파라미터는 한개만 받는데, path 가 value 를 비교 (대소문자 구분 X) 위에 것과 쿼리생성의 차이임. like '%${v}%' (부분검색 허용)
        // bindings.bind(root.title).first((path, value) -> path.likeIgnoreCase(value)); // 검색파라미터는 한개만 받는데, path 가 value 를 비교 (대소문자 구분 X) like '${v}'
        bindings.bind(root.content).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.hashtag).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.createdAt).first(DateTimeExpression::eq);
        bindings.bind(root.createdBy).first(StringExpression::containsIgnoreCase);
    }

}
