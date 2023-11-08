package com.jejoonlee.movmag.app.review.dto;

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
    private LocalDateTime registeredAt;
    private LocalDateTime updatedAt;

}
