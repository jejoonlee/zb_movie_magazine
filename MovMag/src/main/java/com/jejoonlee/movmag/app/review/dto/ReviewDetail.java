package com.jejoonlee.movmag.app.review.dto;

import com.jejoonlee.movmag.app.review.domain.ReviewEntity;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewDetail {
    private Long reviewId;
    private String movieTitleEng;
    private String movieTitleKor;
    private Double movieScore;
    private String author;
    private String reviewTitle;
    private String reviewOneline;
    private String content;
    private LocalDateTime updatedAt;

    public static ReviewDetail fromEntity(ReviewEntity reviewEntity) {
        return ReviewDetail.builder()
                .reviewId(reviewEntity.getReviewId())
                .movieTitleEng(reviewEntity.getMovieEntity().getTitleEng())
                .movieTitleKor(reviewEntity.getMovieEntity().getTitleKor())
                .author(reviewEntity.getMemberEntity().getUsername())
                .reviewTitle(reviewEntity.getReviewTitle())
                .reviewOneline(reviewEntity.getReviewOneline())
                .movieScore(reviewEntity.getMovieScore())
                .updatedAt(reviewEntity.getUpdatedAt())
                .build();
    }
}
