package com.jejoonlee.movmag.app.movie.controller;

import com.jejoonlee.movmag.app.elasticsearch.dto.CastElsDto;
import com.jejoonlee.movmag.app.elasticsearch.dto.MovieElsDto;
import com.jejoonlee.movmag.app.elasticsearch.service.CastSearchService;
import com.jejoonlee.movmag.app.elasticsearch.service.MovieSearchService;
import com.jejoonlee.movmag.app.movie.dto.UpdateMovie;
import com.jejoonlee.movmag.app.movie.service.MovieService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/movie")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;
    private final MovieSearchService movieSearchService;
    private final CastSearchService castSearchService;

    // http://localhost:8080/movie/firstTime
    // 처음에 영화 가지고 오기 (500개, 한번만)
    // only admin (업데이트까지 다 완성되면 권한 넣기)
    @PostMapping("/first-time")
    public UpdateMovie.Response saveAllMovie (
            @RequestBody UpdateMovie.Request request
    ) throws ParseException, InterruptedException {

        return movieService.saveAllMovies(request);
    }


    // 2달 안 최신 영화 가지고 오기 (영화 업데이트 할때마다)
    // only admin
    @PostMapping("/newUpdate")
    public UpdateMovie.Response updateMovie (
            @RequestBody UpdateMovie.Request request
    ) throws ParseException, InterruptedException {

        return movieService.updateNewMovies(request);
    }

    // only admin
    @PostMapping("/save_movie_document")
    public Long saveMovieDocument(){
        return movieSearchService.saveAllMoviesToMovieDocument();
    }

    // only admin
    @PostMapping("/save_cast_document")
    public Long saveCastDocument(){
        return castSearchService.saveAllCastsToCastDocument();
    }

    // all
    @GetMapping("")
    public ResponseEntity<MovieElsDto.PageInfo> searchMovie(
            @RequestParam String movieName,
            @RequestParam String lang,
            @RequestParam int page
    ) {
        return ResponseEntity.ok(movieSearchService.searchMovie(movieName, lang, page));
    }

    @GetMapping("/cast")
    public ResponseEntity<CastElsDto.Page> searchMovieByCast(
            @RequestParam String name,
            @RequestParam int page
    ){
        return ResponseEntity.ok(castSearchService.searchByCast(name, page));
    }
}
