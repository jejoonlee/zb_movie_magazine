package com.jejoonlee.movmag.app.movie.dto;


import lombok.Getter;

import java.util.List;

public class MovieExternalApiDto {

    @Getter
    public static class GenreList{
        private List<Genre> genres;
    }

    @Getter
    public static class Genre{
        private Long id;
        private String name;
    }

//    @Getter
//    @Setter
//    @AllArgsConstructor
//    @NoArgsConstructor
//    public class MovieList{
//        private int page;
//        private int total_pages;
//        private int total_results;
//        private List<MovieInfo> results;
//    }
//
//    @Getter
//    @Setter
//    @AllArgsConstructor
//    @NoArgsConstructor
//    public class MovieInfo {
//
//    }
}
