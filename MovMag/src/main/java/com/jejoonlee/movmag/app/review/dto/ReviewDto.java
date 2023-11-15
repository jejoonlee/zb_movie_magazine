package com.jejoonlee.movmag.app.review.dto;

import com.jejoonlee.movmag.app.member.domain.MemberEntity;
import com.jejoonlee.movmag.app.movie.domain.MovieEntity;
import com.jejoonlee.movmag.app.review.domain.ReviewEntity;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {

    private MovieEntity movieEntity;
    private MemberEntity memberEntity;
    private String reviewTitle;
    private String reviewOneline;
    private Double movieScore;
    private String content;
    private LocalDateTime registeredAt;
    private LocalDateTime updatedAt;

    public ReviewEntity toEntity(){
        return ReviewEntity.builder()
                .movieEntity(movieEntity)
                .memberEntity(memberEntity)
                .reviewTitle(reviewTitle)
                .reviewOneline(reviewOneline)
                .movieScore(movieScore)
                .content(content)
                .registeredAt(registeredAt)
                .updatedAt(updatedAt)
                .build();
    }

    public ReviewDto fromEntity(ReviewEntity reviewEntity){
        return ReviewDto.builder()
                .movieEntity(reviewEntity.getMovieEntity())
                .memberEntity(reviewEntity.getMemberEntity())
                .reviewTitle(reviewEntity.getReviewTitle())
                .reviewOneline(reviewEntity.getReviewOneline())
                .movieScore(reviewEntity.getMovieScore())
                .content(reviewEntity.getContent())
                .registeredAt(reviewEntity.getRegisteredAt())
                .updatedAt(reviewEntity.getUpdatedAt())
                .build();
    }

}
