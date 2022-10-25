package com.fastcampus.projectboard.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

@EnableJpaAuditing // JPA 오디팅 기능을 추가 활성화 시킴
@Configuration
public class JpaConfig {

    @Bean
    public AuditorAware<String> auditorAware() { // 제네릭에는 사람이름을 쓸 것이니 String
        return () -> Optional.of("revi1337"); // TODO: 스프링 시큐리티로 인증 기능을 붙이게 될 때, 수정하자.
    }
}
