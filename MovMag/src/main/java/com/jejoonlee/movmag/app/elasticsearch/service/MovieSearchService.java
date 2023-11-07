package com.jejoonlee.movmag.app.elasticsearch.service;

import com.jejoonlee.movmag.app.elasticsearch.dto.MovieElsDto;

public interface MovieSearchService {

    Long saveAllMoviesToMovieDocument();
    MovieElsDto.PageInfo searchMovie(String movieName, String lang, int page);
}
