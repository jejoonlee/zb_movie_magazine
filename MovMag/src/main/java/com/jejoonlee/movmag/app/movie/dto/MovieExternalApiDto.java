package com.jejoonlee.movmag.app.movie.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
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

        @JsonProperty("total_pages")
        private int totalPages;

        @JsonProperty("total_results")
        private int totalResults;
        private List<MovieInfo> results;
    }

    @Getter
    @Setter
    public static class MovieInfo {
        private Long id;

        @JsonProperty("genre_ids")
        private List<Long> genreIds;

        private String title;
        private String overview;

        @JsonProperty("release_date")
        private String releaseDate;

        @JsonProperty("poster_path")
        private String posterPath;
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

        @JsonProperty("known_for_department")
        private String knownForDepartment;
    }

}
