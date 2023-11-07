package com.jejoonlee.movmag.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AppConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl("https://api.themoviedb.org/3")
                .defaultHeader(HttpHeaders.ACCEPT, "application/json")
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(-1))
                .build();
    }
}
