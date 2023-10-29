package com.jejoonlee.movmag.app.movie.service;

import com.jejoonlee.movmag.app.movie.dto.UpdateMovie;
import org.json.simple.parser.ParseException;

public interface MovieService {

    UpdateMovie.Response saveAllMovies(UpdateMovie.Request request) throws ParseException;
}
