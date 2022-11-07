package com.fastcampus.projectboard.service;

import com.fastcampus.projectboard.domain.Article;
import com.fastcampus.projectboard.domain.type.SearchType;
import com.fastcampus.projectboard.dto.ArticleDto;
import com.fastcampus.projectboard.dto.ArticleUpdateDto;
import com.fastcampus.projectboard.repository.ArticleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@DisplayName("비지니스 로직 - 게시글")
@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {

    @InjectMocks // Mock 을 주입하는 클래스에 붙임 (// 테스트 대상 설정)
    private ArticleService sut;

    @Mock // 그 외 나머지는 Mock 이라는 어노테이션을 붙임
    private ArticleRepository articleRepository;


    @DisplayName("게시글을 검색하면 게시글 리스트를 반환한다. [게시판 기능]")
    @Test
    void givenSearchParameters_whenSearchingArticles_thenReturnArticleList() {
        // given

        // when
        // List<ArticleDto> articles = sut.searchArticles(SearchType.TITLE, "search keyword"); // 제목, 본문, id, nickname, 해시태그
        Page<ArticleDto> articles = sut.searchArticles(SearchType.TITLE, "search keyword"); // 제목, 본문, id, nickname, 해시태그

        // then
        assertThat(articles).isNotNull();
    }

    @DisplayName("게시글을 조회하면, 게시글을 반환한다. [게시판 기능]")
    @Test
    void givenArticleId_whenSearchingArticle_thenReturnArticle() {
        // given

        // when
        ArticleDto articles = sut.searchArticle(1L);

        // then
        assertThat(articles).isNotNull();
    }

    @DisplayName("게시글 정보를 입력하면, 게시글을 생성한다 [게시글 기능]")
    @Test
    void givenArticleInfo_whenSavingArticle_thenSavesArticle(){
        // Given
        given(articleRepository.save(any(Article.class))).willReturn(null); // given(BDD 꺼), willDoNothing(BDD 꺼), any(BDD 꺼) 을 통해 Mock 객체에 접근할 수 있음. 최상단의 articleRepository 를 말함.

        // When
        sut.saveArticle(ArticleDto.of(LocalDateTime.now(), "Uno", "title", "content", "#java"));

        // Then
        then(articleRepository).should().save(any(Article.class)); // then(BDD 꺼), should(BDD 꺼), any(BDD 꺼) : 를 통해 실제로 articleRepository 에서 save 를 호출었는지를 검사.
    }

    @DisplayName("게시글의 Id 와 수정정보를 입력하면, 게시글을 수정한다 [게시글 기능]")
    @Test
    void givenArticleIdAndModifiedInfo_whenUpdatingArticle_thenUpdatesArticle() {
        // Given
        given(articleRepository.save(any(Article.class))).willReturn(null); // given(BDD 꺼), willDoNothing(BDD 꺼), any(BDD 꺼) 을 통해 Mock 객체에 접근할 수 있음. 최상단의 articleRepository 를 말함.

        // When
        sut.updateArticle(1L, ArticleUpdateDto.of("title", "content", "#java"));

        // Then
        then(articleRepository).should().save(any(Article.class)); // then(BDD 꺼), should(BDD 꺼), any(BDD 꺼) : 를 통해 실제로 articleRepository 에서 save 를 호출었는지를 검사.
    }

    @DisplayName("게시글의 ID를 입력하면, 게시글을 삭제한다. [게시글 기능]")
    @Test
    void givenArticleId_whenDeletingArticle_thenDeletesArticle() {
        // Given
        willDoNothing().given(articleRepository).delete(any(Article.class)); // given(BDD 꺼), willDoNothing(BDD 꺼), any(BDD 꺼) 을 통해 Mock 객체에 접근할 수 있음. 최상단의 articleRepository 를 말함.

        // When
        sut.deleteArticle(1L);

        // Then
        then(articleRepository).should().delete(any(Article.class)); // then(BDD 꺼), should(BDD 꺼), any(BDD 꺼) : 를 통해 실제로 articleRepository 에서 save 를 호출었는지를 검사.
    }
}
