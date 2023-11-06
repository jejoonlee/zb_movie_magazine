package com.jejoonlee.movmag.app.movie.service.impl;

import com.jejoonlee.movmag.app.movie.dto.MovieExternalApiDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@RequiredArgsConstructor
@Service
public class MovieExternalApiClient {
    private static final WebClient webClient = WebClient.builder()
            .baseUrl("https://api.themoviedb.org/3")
            .defaultHeader(HttpHeaders.ACCEPT, "application/json")
            .build();

    static MovieExternalApiDto.GenreList getGenre(String apiKey, String language) {

        MovieExternalApiDto.GenreList response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/genre/movie/list")
                        .queryParam("language", language)
                        .build())
                .header(HttpHeaders.AUTHORIZATION, apiKey)
                .retrieve()
                .bodyToMono(MovieExternalApiDto.GenreList.class)
                .block();

        return response;
    }


    public void test(String apiKey) {

        log.info("=============start webclient=============");

        MovieExternalApiDto.GenreList response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/genre/movie/list")
                        .queryParam("language", "en")
                        .build())
                .header(HttpHeaders.AUTHORIZATION, apiKey)
                .retrieve()
                .bodyToMono(MovieExternalApiDto.GenreList.class)
                .block();

        log.info("{}", response.getGenres().get(0).getName());

        log.info("=============end webclient============");
    }
}
