package com.fastcampus.projectboard.repository;

import com.fastcampus.projectboard.domain.ArticleComment;
import com.fastcampus.projectboard.domain.QArticleComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface ArticleCommentRepository extends
        JpaRepository<ArticleComment, Long>,
        QuerydslPredicateExecutor<ArticleComment>, // 기본적으로 Article 안에있는 모든 Field 에 대한 기본 검색 기능을 추가해줌. (사실 이거 하나만 넣어도 검색 기능은 끝남)
        QuerydslBinderCustomizer<QArticleComment>  // QueryBindCustomizer 에 들어가는 제네릭은 QClass 넣어주게 되어있음. (이것의 기능은 입맛에 맞게 검색기능을 추가할 수 있음.)
{
    List<ArticleComment> findByArticle_Id(Long articleId);

    @Override
    default void customize(QuerydslBindings bindings, QArticleComment root){
        bindings.excludeUnlistedProperties(true); // 현재 QuerydslPredicateExecutor 기능에 의해서 Article 에 있는 모든 Field 들에 대한 검색이 열려있는데, 이를 선택적인 Field 로 검색을 가능하게 해줌.
        bindings.including(root.content, root.createdAt, root.createdBy); // 요놈으로 원하는 필드들을 선택해 줄 수 있음.
        bindings.bind(root.content).first((path, value) -> path.containsIgnoreCase(value)); // 검색파라미터는 한개만 받는데, path 가 value 를 비교 (대소문자 구분 X) 위에 것과 쿼리생성의 차이임. like '%${v}%' (부분검색 허용)
        bindings.bind(root.createdAt).first((path, value) -> path.eq(value));
        bindings.bind(root.createdBy).first((path, value) -> path.containsIgnoreCase(value));
    }

}
