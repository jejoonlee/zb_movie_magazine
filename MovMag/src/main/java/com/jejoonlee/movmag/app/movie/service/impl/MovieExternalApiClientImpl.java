package com.jejoonlee.movmag.app.movie.service.impl;

import com.jejoonlee.movmag.app.movie.dto.MovieExternalApiDto;
import com.jejoonlee.movmag.app.movie.service.MovieExternalApiClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@RequiredArgsConstructor
public class MovieExternalApiClientImpl implements MovieExternalApiClient {
    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://api.themoviedb.org/3")
            .defaultHeader(HttpHeaders.ACCEPT, "application/json")
            .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(-1))
            .build();


    public MovieExternalApiDto.GenreList getGenre(String apiKey, String language) {

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

    public MovieExternalApiDto.MovieList getMovieList(String apiKey, String language, String page) {

        MovieExternalApiDto.MovieList response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/movie/popular")
                        .queryParam("language", language)
                        .queryParam("page", page)
                        .build())
                .header(HttpHeaders.AUTHORIZATION, apiKey)
                .retrieve()
                .bodyToMono(MovieExternalApiDto.MovieList.class)
                .block();

        return response;
    }

    // 캐스트들 정보와 런타임 정보
    public MovieExternalApiDto.MovieDetail getMovieDetail(String apiKey, Long movieId) {

        MovieExternalApiDto.MovieDetail response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/movie/" + String.valueOf(movieId))
                        .queryParam("append_to_response", "credits")
                        .build())
                .header(HttpHeaders.AUTHORIZATION, apiKey)
                .retrieve()
                .bodyToMono(MovieExternalApiDto.MovieDetail.class)
                .block();

        return response;
    }
}
