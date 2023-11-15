package com.jejoonlee.movmag.app.review.dto;

import com.jejoonlee.movmag.app.member.domain.MemberEntity;
import com.jejoonlee.movmag.app.review.domain.LikeEntity;
import com.jejoonlee.movmag.app.review.domain.ReviewEntity;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewLikeDto {

    private ReviewEntity reviewEntity;
    private MemberEntity memberEntity;

    public static LikeEntity toEntity(ReviewLikeDto reviewLikeDto) {
        return LikeEntity.builder()
                .reviewEntity(reviewLikeDto.reviewEntity)
                .memberEntity(reviewLikeDto.memberEntity)
                .build();
    }

    public static ReviewLikeDto fromEntity(LikeEntity likeEntity) {
        return ReviewLikeDto.builder()
                .reviewEntity(likeEntity.getReviewEntity())
                .memberEntity(likeEntity.getMemberEntity())
                .build();
    }
}
