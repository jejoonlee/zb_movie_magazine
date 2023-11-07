package com.jejoonlee.movmag.app.elasticsearch.dto;

import com.jejoonlee.movmag.app.elasticsearch.document.MovieDocument;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

public class MovieElsDto {

    @Getter
    @Setter
    @Builder
    public static class PageInfo {
        private int page;
        private int totalPage;
        private Long foundDataNum;
        private List<Response> data;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {

        private Long number;
        private String titleEng;
        private String titleKor;
        private Long movieId;
        private List<String> genre;
        private String overviewEng;
        private String overviewKor;
        private LocalDate releasedDate;
        private Double movieScore;
        private Long runtime;
        private String posterPath;

        public static Response fromDocument(MovieDocument movieDocument) {

            String posterPathUrl = "https://image.tmdb.org/t/p/original";
            String posterPath = posterPathUrl + movieDocument.getPosterPath();

            return Response.builder()
                    .movieId(movieDocument.getMovieId())
                    .titleEng(movieDocument.getTitleEng())
                    .titleKor(movieDocument.getTitleKor())
                    .overviewEng(movieDocument.getOverviewEng())
                    .overviewKor(movieDocument.getOverviewKor())
                    .releasedDate(movieDocument.getReleasedDate())
                    .movieScore(movieDocument.getMovieScore())
                    .runtime(movieDocument.getRuntime())
                    .posterPath(posterPath)
                    .build();
        }

    }
}
