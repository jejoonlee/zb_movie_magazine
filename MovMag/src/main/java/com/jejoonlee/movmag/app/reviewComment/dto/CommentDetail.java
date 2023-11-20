package com.jejoonlee.movmag.app.reviewComment.dto;

import com.jejoonlee.movmag.app.review.domain.ReviewEntity;
import com.jejoonlee.movmag.app.reviewComment.domain.CommentEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

public class CommentDetail {

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
    @Builder
    public static class Response {
        private Long reviewId;
        private String reviewName;
        private String movieName;
        private String content;
        private LocalDateTime updatedAt;

        public static Response fromEntity(CommentEntity comment) {

            ReviewEntity review = comment.getReviewEntity();
            return Response.builder()
                    .reviewId(review.getReviewId())
                    .reviewName(review.getReviewTitle())
                    .movieName(review.getMovieEntity().getTitleEng())
                    .content(comment.getContent())
                    .updatedAt(comment.getUpdatedAt())
                    .build();
        }
    }
}
