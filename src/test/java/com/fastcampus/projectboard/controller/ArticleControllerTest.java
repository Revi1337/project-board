package com.fastcampus.projectboard.controller;

import com.fastcampus.projectboard.config.SecurityConfig;
import com.fastcampus.projectboard.dto.ArticleWithCommentsDto;
import com.fastcampus.projectboard.dto.UserAccountDto;
import com.fastcampus.projectboard.service.ArticleService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// 본격적인 웹 mvc 테스트
@DisplayName("View 컨트롤러 - 게시글")
@Import(SecurityConfig.class)   // 사전에 셋팅한 시큐리티 설정을 통해서 인증에 막히지 않고 테스트를 할 수 있음.
@WebMvcTest(ArticleController.class) // 테스트 대상이 되는 controller 만 Bean 으로 읽어들이는 것이 가능함.
class ArticleControllerTest {

    private final MockMvc mvc;

    @MockBean private ArticleService articleService;


    public ArticleControllerTest(@Autowired MockMvc mvc) {  // 테스트 패키지에 있는 애는 생정자가 하나만 있을 때, @Autowired 를 생략 불가.
        this.mvc = mvc;
    }

    //@Disabled("구현 중") // build 는 내부적으로 테스트가 통과되어야 가능함. 하지만, 개발중이면, 통과하지 않기 떄문에, 이를 각 단위 메서드 별로 테스트를 제외시킬 수 있음.
    @DisplayName("[view] [GET] 게시글 리스트 (게시판) 페이지 - 정상 호출")
    @Test
    public void givenNothing_whenRequestingArticlesView_thenReturnsArticlesView() throws Exception {
        // Given
        given(articleService.searchArticles(eq(null), eq(null), any(Pageable.class)))
                .willReturn(Page.empty());

        // When & Then
        mvc.perform(get("/articles")) // 해당 경로로 요청을 하면
                .andExpect(status().isOk()) // 200 을 검사하고
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML)) // Content-type 이 text/html 인지 부분검사하고. ( contentType(MediaType.TEXT_HTML)) 와의 차이점은 호환되는 타입까지 전부 다 맞다고 침. (text/html 과 text/html;charset=UTF-8 등))
                .andExpect(view().name("articles/index")) // view 의 이름은 template 폴더 하위 articles 하위 index (html) 이어야 하고 ( view 의 이름에 대한 테스트도 작성 가능)
                .andExpect(model().attributeExists("articles")); // Data 로 articles 이란 것을 넘겨주어야 한다.

        then(articleService).should().searchArticles(eq(null), eq(null), any(Pageable.class));
    }

//    @Disabled("구현 중")
    @DisplayName("[view] [GET] 게시글 상세 페이지 - 정상 호출")
    @Test
    public void givenNothing_whenRequestingArticleView_thenReturnsArticleView() throws Exception {
        // Given
        Long articleId = 1L;
        given(articleService.getArticle(articleId)).willReturn(createArticleWithCommentsDto());

        // When & Then
        mvc.perform(get("/articles/" + articleId))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/detail"))
                .andExpect(model().attributeExists("article"))
                .andExpect(model().attributeExists("articleComments"));
        then(articleService).should().getArticle(articleId);
    }

    @Disabled("구현 중")
    @DisplayName("[view] [GET] 게시글 검색 전용 페이지 - 정상 호출")
    @Test
    public void givenNothing_whenRequestingArticleSearchView_thenReturnsArticleSearchView() throws Exception {
        // Given

        // When & Then
        mvc.perform(get("/articles/search"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(model().attributeExists("articles/search"));
    }

    @Disabled("구현 중")
    @DisplayName("[view] [GET] 게시글 해시태그 검색 페이지 - 정상 호출")
    @Test
    public void givenNothing_whenRequestingArticleHashtagSearchView_thenReturnsArticleHashtagSearchView() throws Exception {
        // Given

        // When & Then
        mvc.perform(get("/articles/search"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(model().attributeExists("articles/search-hashtag"));
    }

    private ArticleWithCommentsDto createArticleWithCommentsDto() {
        return ArticleWithCommentsDto.of(
                1L,
                createUserAccountDto(),
                Set.of(),
                "title",
                "content",
                "#java",
                LocalDateTime.now(),
                "uno",
                LocalDateTime.now(),
                "uno"
        );
    }

    private UserAccountDto createUserAccountDto() {
        return UserAccountDto.of(
                1L,
                "uno",
                "pw",
                "uno@mail.com",
                "Uno",
                "memo",
                LocalDateTime.now(),
                "uno",
                LocalDateTime.now(),
                "uno"
        );
    }

}
