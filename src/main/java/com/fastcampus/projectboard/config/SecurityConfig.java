package com.fastcampus.projectboard.config;

import com.fastcampus.projectboard.dto.UserAccountDto;
import com.fastcampus.projectboard.dto.security.BoardPrincipal;
import com.fastcampus.projectboard.repository.UserAccountRepository;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(UserAccountRepository userAccountRepository) {
        return username -> userAccountRepository
                .findById(username)
                .map(UserAccountDto::from)
                .map(BoardPrincipal::from)
                .orElseThrow(() -> new UsernameNotFoundException("유저를 찾을 수 없습니다." + username));
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        .mvcMatchers(HttpMethod.GET, "/", "/articles", "/articles/search-hashtag").permitAll()
                        .anyRequest().authenticated())
                .formLogin().and()
                .logout(logout -> logout
                        .logoutSuccessUrl("/"))
                .build();
    }

}


// TODO WARN 12584 --- [  restartedMain] o.s.s.c.a.web.builders.WebSecurity - This is not recommended -- please use permitAll via HttpSecurity#authorizeHttpRequests instead
// TODO 아래 리소스들은 스프링시큐리티의 보안필터들의 영역에 빠지게되어, CSRF SSTI, SSRF, XSS 등 보안공격에 취약해짐. 따라서 HttpSecurity 설정에 통쨰로 떄리라고 추천함.
//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        return web -> web
//                .ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
//    }
