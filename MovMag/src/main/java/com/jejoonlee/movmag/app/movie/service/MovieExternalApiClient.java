package com.jejoonlee.movmag.app.movie.service;

import com.jejoonlee.movmag.app.movie.dto.MovieExternalApiDto;

public interface MovieExternalApiClient {

    public MovieExternalApiDto.GenreList getGenre(String apiKey, String language);

    public MovieExternalApiDto.MovieList getMovieList(String apiKey, String language, String page);

    public MovieExternalApiDto.MovieDetail getMovieDetail(String apiKey, Long movieId);
}
