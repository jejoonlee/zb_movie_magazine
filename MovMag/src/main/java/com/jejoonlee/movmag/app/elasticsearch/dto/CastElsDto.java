package com.jejoonlee.movmag.app.elasticsearch.dto;

import com.jejoonlee.movmag.app.elasticsearch.document.CastDocument;
import com.jejoonlee.movmag.app.movie.dto.MovieDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class CastElsDto {

    @Builder
    @Getter
    @Setter
    public static class Page {
        private int page;
        private int totalPage;
        private Long foundDataNum;
        private List<Response> data;
    }

    @Builder
    @Getter
    @Setter
    public static class Movie {
        private Long movieId;
        private String titleEng;
        private String titleKor;

        public static Movie fromMovieDto(MovieDto movieDto) {
            return Movie.builder()
                    .movieId(movieDto.getMovieId())
                    .titleEng(movieDto.getTitleEng())
                    .titleKor(movieDto.getTitleKor())
                    .build();
        }
    }

    @Builder
    @Getter
    @Setter
    public static class Response {
        private String name;
        private Long castId;
        private List<Movie> featuredMovies;
        private String role;

        public static Response fromDocument(CastDocument castDocument){
            return Response.builder()
                    .name(castDocument.getNameEng())
                    .castId(castDocument.getCastId())
                    .role(castDocument.getRole())
                    .build();
        }
    }
}
