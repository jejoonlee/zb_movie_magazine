package com.jejoonlee.movmag.app.review.dto;

import com.jejoonlee.movmag.app.review.domain.ReviewEntity;
import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

public class ReviewRegister {

    @Builder
    @Getter
    @Setter
    public static class Request {

        @Positive(message="영화 ID를 가지고 와주세요")
        private Long movieId;

        @NotBlank(message="리뷰 제목을 입력해주세요")
        @Size(max=50, message = "50자 이하여야 합니다")
        private String reviewTitle;

        @NotBlank(message="리뷰에 대한 간단한 설명을 넣어주세요")
        @Size(max=200, message = "100자 이하여야 합니다")
        private String reviewOneline;

        @Positive(message="영화 점수를 넣어주세요. 1점~5점 사이")
        @Max(value = 5)
        @Min(value = 1)
        private double movieScore;

        @NotBlank(message="리뷰를 써주세요")
        @Size(max=5000, message = "5000자를 넘으셨습니다")
        private String content;
    }

    @Getter
    @Setter
    @Builder
    public static class Update{

        @Positive(message="리뷰 ID를 입력해주세요")
        private Long reviewId;

        @Positive(message="영화 ID를 가지고 와주세요")
        private Long movieId;

        @NotBlank(message="리뷰 제목을 입력해주세요")
        @Size(max=50, message = "50자 이하여야 합니다")
        private String reviewTitle;

        @NotBlank(message="리뷰에 대한 간단한 설명을 넣어주세요")
        @Size(max=200, message = "100자 이하여야 합니다")
        private String reviewOneline;

        @Positive(message="영화 점수를 넣어주세요. 1점~5점 사이")
        @Max(value = 5)
        @Min(value = 1)
        private double movieScore;

        @NotBlank(message="리뷰를 써주세요")
        @Size(max=5000, message = "5000자를 넘으셨습니다")
        private String content;
    }

    @Builder
    @Getter
    @Setter
    public static class Response {
        private Long reviewId;
        private String reviewTitle;
        private String author;
        private String reviewOneline;
        private Double movieScore;
        private LocalDateTime registeredAt;
        private LocalDateTime updatedAt;

        public static Response fromEntity(ReviewEntity reviewEntity){
            return Response.builder()
                    .reviewId(reviewEntity.getReviewId())
                    .reviewTitle(reviewEntity.getReviewTitle())
                    .author(reviewEntity.getMemberEntity().getUsername())
                    .reviewOneline(reviewEntity.getReviewOneline())
                    .movieScore(reviewEntity.getMovieScore())
                    .registeredAt(reviewEntity.getRegisteredAt())
                    .updatedAt(reviewEntity.getUpdatedAt())
                    .build();
        }
    }
}
