package com.jejoonlee.movmag.app.movie.controller;

import com.jejoonlee.movmag.app.movie.dto.UpdateMovie;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/movie")
public class MovieController {

    // 처음에 영화 가지고 오기 (500개, 한번만)
    // only admin
    @PostMapping("/firstTime")
    public UpdateMovie.Response saveAllMovie (
            @RequestBody UpdateMovie.Request request
    ) {
        return null;
    }


    // 2달 안 최신 영화 가지고 오기 (영화 업데이트 할때마다)
    // only admin

}
