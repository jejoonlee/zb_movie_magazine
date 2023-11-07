package com.jejoonlee.movmag.app.elasticsearch.service;

import com.jejoonlee.movmag.app.elasticsearch.dto.MovieElsDto;

import java.util.List;

public interface MovieSearchService {

    Long saveAllMoviesToMovieDocument();
    List<MovieElsDto.Response> searchMovie(String movieName, String lang);
}
