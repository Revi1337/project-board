package com.fastcampus.projectboard.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

// 스프링 시큐리티를 사용할때는 이 파일과 같이 Configuration 을 하지 않으면 서버가 동작했을떄, 인증을 하지 않으면 아무것도 할 수 없느 상태가 됨.
// 하지만 Configuration 을 해주면 login 페이지를 별도로 뺴줄 수 있음.

// security 를 설정하는 "extends WebSecurityConfigurerAdapter" 는 나중에 사라질 예정이므로, Bean 컴포넌트로 관리하는 방법으로 쓰는 것이 좋음.
// WebSecurityConfigurerAdapter 를 사용할떄는 @EnableWebSecurity 사용해야하지만, Bean 컴포넌트로 관리할때는 써주지 않아도 됨.
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll()) // 모든 Request 에 대해 인증을 다 열겠다.
                .formLogin().and()  // /login 폼 페이지를 만들게끔 유도
                .build();   // 마지막으로 빌드.
    }
}
