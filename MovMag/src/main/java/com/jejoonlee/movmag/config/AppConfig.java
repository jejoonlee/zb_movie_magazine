package com.jejoonlee.movmag.config;

import com.jejoonlee.movmag.app.movie.service.MovieExternalApiClient;
import com.jejoonlee.movmag.app.movie.service.impl.MovieExternalApiClientImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AppConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public MovieExternalApiClient movieExternalApiClient() {
        return new MovieExternalApiClientImpl();
    }
}
