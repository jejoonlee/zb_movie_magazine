package com.jejoonlee.movmag.app.review.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewPopular {
    private Long likeCount;
    private ReviewRegister.Response reviewBrief;
}
