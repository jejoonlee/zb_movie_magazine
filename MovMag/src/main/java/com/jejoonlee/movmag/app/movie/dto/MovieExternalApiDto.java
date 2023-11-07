package com.jejoonlee.movmag.app.movie.dto;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class MovieExternalApiDto {

    @Getter
    @Setter
    public static class GenreList{
        private List<Genre> genres;
    }

    @Getter
    @Setter
    public static class Genre{
        private Long id;
        private String name;
    }

    @Getter
    @Setter
    public static class MovieList{
        private int page;
        private int total_pages;
        private int total_results;
        private List<MovieInfo> results;
    }

    @Getter
    @Setter
    public static class MovieInfo {
        private Long id;
        private List<Long> genre_ids;
        private String title;
        private String overview;
        private String release_date;
        private String poster_path;
    }

    @Getter
    @Setter
    public static class MovieDetail {
        private Long runtime;
        private Credits credits;
    }

    @Getter
    public static class Credits {
        private List<CastInfo> cast;
    }

    @Getter
    @Setter
    public static class CastInfo{
        private Long id;
        private String name;
        private String known_for_department;
    }

}
