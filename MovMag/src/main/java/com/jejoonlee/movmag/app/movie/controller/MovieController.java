package com.jejoonlee.movmag.app.movie.controller;

import com.jejoonlee.movmag.app.elasticsearch.dto.CastElsDto;
import com.jejoonlee.movmag.app.elasticsearch.dto.MovieElsDto;
import com.jejoonlee.movmag.app.elasticsearch.service.CastSearchService;
import com.jejoonlee.movmag.app.elasticsearch.service.MovieSearchService;
import com.jejoonlee.movmag.app.movie.dto.UpdateMovie;
import com.jejoonlee.movmag.app.movie.service.MovieService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/movie")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;
    private final MovieSearchService movieSearchService;
    private final CastSearchService castSearchService;

    // http://localhost:8080/movie/save
    // 처음에 영화 가지고 오기 (500개, 한번만)
    // only admin (업데이트까지 다 완성되면 권한 넣기)
    @PostMapping("/save")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiOperation(value="TMDB API에서 영화 저장. Admin만만 이용 가능")
    public UpdateMovie.Response saveAllMovie (
            @RequestBody UpdateMovie.Request request
    ) throws ParseException, InterruptedException {

        return movieService.saveAllMovies(request);
    }


    // 2달 안 최신 영화 가지고 오기 (영화 업데이트 할때마다)
    // only admin
    @PostMapping("/update")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiOperation(value="TMDB API에서 최신 영화 업데이트. Admin만 이용 가능")
    public UpdateMovie.Response updateMovie (
            @RequestBody UpdateMovie.Request request
    ) throws ParseException, InterruptedException {

        return movieService.updateNewMovies(request);
    }

    // only admin
    @PostMapping("/document/save-movie")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiOperation(value="저장된 모든 영화 정보를 Elasticsearch에 저장. Admin만 이용 가능")
    public Long saveMovieDocument(){
        return movieSearchService.saveAllMoviesToMovieDocument();
    }

    // only admin
    @PostMapping("/document/save-cast")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiOperation(value="캐스트 정보를 Elasticsearch에 저장. Admin만 가능")
    public Long saveCastDocument(){
        return castSearchService.saveAllCastsToCastDocument();
    }

    // all
    @GetMapping("")
    @ApiOperation(value="영화 이름을 검색하여 영화 찾기")
    public ResponseEntity<MovieElsDto.PageInfo> searchMovie(
            @RequestParam String title,
            @RequestParam String lang,
            @RequestParam int page
    ) {
        return ResponseEntity.ok(movieSearchService.searchMovie(title, lang, page));
    }

    @GetMapping("/cast")
    @ApiOperation(value="캐스트를 검색하여, 캐스트가 나온 영화 찾기")
    public ResponseEntity<CastElsDto.Page> searchMovieByCast(
            @RequestParam String name,
            @RequestParam int page
    ){
        return ResponseEntity.ok(castSearchService.searchByCast(name, page));
    }
}
