package com.fastcampus.projectboard.controller;

import com.fastcampus.projectboard.config.SecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@DisplayName("View 컨트롤러 - 인증")
@Import(SecurityConfig.class)   // 사전에 셋팅한 시큐리티 설정을 통해서 인증에 막히지 않고 테스트를 할 수 있음.
@WebMvcTest
public class AuthControllerTest {

    private final MockMvc mvc;

    public AuthControllerTest(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }

    @DisplayName("[view] [GET] 로그인 페이지 - 정상 호출")
    @Test
    public void givenNothing_whenTryingToLogIn_thenReturnsLogInView() throws Exception {
        // Given

        // When & Then
        mvc.perform(get("/login")) // 해당 경로로 요청을 하면
                .andExpect(status().isOk()) // 200 을 검사하고
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML)); // Content-type 이 text/html 인지 부분검사하고. ( contentType(MediaType.TEXT_HTML)) 와의 차이점은 호환되는 타입까지 전부 다 맞다고 침. (text/html 과 text/html;charset=UTF-8 등))
    }

}
