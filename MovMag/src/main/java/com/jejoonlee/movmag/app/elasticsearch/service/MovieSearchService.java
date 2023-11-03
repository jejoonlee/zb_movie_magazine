package com.jejoonlee.movmag.app.elasticsearch.service;

import com.jejoonlee.movmag.app.elasticsearch.document.MovieDocument;

import java.util.List;

public interface MovieSearchService {

    Long saveAllMoviesToMovieDocument();
    List<MovieDocument> searchMovie(String movieName, String lang);
}
