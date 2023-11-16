package com.jejoonlee.movmag.app.elasticsearch.dto;

import com.jejoonlee.movmag.app.elasticsearch.document.ReviewDocument;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

public class ReviewElsDto {

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
        private Long reviewId;
        private String movieTitleEng;
        private String movieTitleKor;
        private String author;
        private String reviewTitle;
        private String reviewOneline;
        private LocalDateTime updatedAt;

        public static Response fromDocument(ReviewDocument reviewDocument) {
            return Response.builder()
                    .reviewId(reviewDocument.getReviewId())
                    .movieTitleEng(reviewDocument.getMovieTitleEng())
                    .movieTitleKor(reviewDocument.getMovieTitleKor())
                    .author(reviewDocument.getAuthor())
                    .reviewTitle(reviewDocument.getReviewTitle())
                    .reviewOneline(reviewDocument.getReviewOneline())
                    .updatedAt(reviewDocument.getUpdatedAt())
                    .build();
        }
    }
}
