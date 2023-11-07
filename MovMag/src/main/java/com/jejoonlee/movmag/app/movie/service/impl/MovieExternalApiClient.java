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

    private final WebClient webClient;

    public MovieExternalApiDto.GenreList getGenre(String apiKey, String language) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/genre/movie/list")
                        .queryParam("language", language)
                        .build())
                .header(HttpHeaders.AUTHORIZATION, apiKey)
                .retrieve()
                .bodyToMono(MovieExternalApiDto.GenreList.class)
                .block();
    }

    public MovieExternalApiDto.MovieList getMovieList(String apiKey, String language, String page) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/movie/popular")
                        .queryParam("language", language)
                        .queryParam("page", page)
                        .build())
                .header(HttpHeaders.AUTHORIZATION, apiKey)
                .retrieve()
                .bodyToMono(MovieExternalApiDto.MovieList.class)
                .block();
    }

    // 캐스트들 정보와 런타임 정보
    public MovieExternalApiDto.MovieDetail getMovieDetail(String apiKey, Long movieId) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/movie/" + String.valueOf(movieId))
                        .queryParam("append_to_response", "credits")
                        .build())
                .header(HttpHeaders.AUTHORIZATION, apiKey)
                .retrieve()
                .bodyToMono(MovieExternalApiDto.MovieDetail.class)
                .block();
    }
}
