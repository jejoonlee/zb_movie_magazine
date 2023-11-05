package com.jejoonlee.movmag.app.movie.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

public class MovieExternalApiDto {

    @Getter
    public static class GenreList{
        private List<GenreEng> genres;
    }

    @Getter
    public static class GenreEng{
        private Long id;
        private String name;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GenreKor{
        private Long genreId;
        private String genreName;
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
