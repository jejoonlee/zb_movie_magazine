package com.jejoonlee.movmag.app.movie.service.impl;

import com.jejoonlee.movmag.app.movie.dto.MovieExternalApiDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@RequiredArgsConstructor
@Service
public class MovieExternalApiClient {

    private String API_KEY;

    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://api.themoviedb.org/3")
            .build();

    public void test(String api) {

        log.info("=============start webclient=============");

        API_KEY = api;

        log.info("{}", API_KEY);
        log.info("{}", webClient.head());

        MovieExternalApiDto.GenreList response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/genre/movie/list")
                        .queryParam("language", "en")
                        .build())
                .header(HttpHeaders.AUTHORIZATION, API_KEY)
                .header(HttpHeaders.ACCEPT, "application/json")
                .retrieve()
                .bodyToMono(MovieExternalApiDto.GenreList.class)
                .block();

        log.info("{}", response.getGenres().get(0).getName());

        log.info("=============end webclient============");
    }
}
