package com.jejoonlee.movmag.app.movie.controller;

import com.jejoonlee.movmag.app.movie.dto.UpdateMovie;
import com.jejoonlee.movmag.app.movie.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/movie")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    // http://localhost:8080/movie/firstTime
    // 처음에 영화 가지고 오기 (500개, 한번만)
    // only admin
    @PostMapping("/first-time")
    public UpdateMovie.Response saveAllMovie (
            @RequestBody UpdateMovie.Request request
    ) throws ParseException, InterruptedException {

        return movieService.saveAllMovies(request);
    }


    // 2달 안 최신 영화 가지고 오기 (영화 업데이트 할때마다)
    // only admin

}
