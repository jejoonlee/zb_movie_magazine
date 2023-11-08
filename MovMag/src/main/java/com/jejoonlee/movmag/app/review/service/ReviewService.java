package com.jejoonlee.movmag.app.review.service;

import com.jejoonlee.movmag.app.review.dto.ReviewDelete;
import com.jejoonlee.movmag.app.review.dto.ReviewDetail;
import com.jejoonlee.movmag.app.review.dto.ReviewRegister;
import org.springframework.security.core.Authentication;

public interface ReviewService {

    ReviewRegister.Response createReview(ReviewRegister.Request request, Authentication authentication);

    ReviewDetail getReviewDetail(Long reviewId, Authentication authentication);

    ReviewRegister.Response updateReview(ReviewRegister.Update update, Authentication authentication);

    ReviewDelete.Response deleteReview(Long reviewId, Authentication authentication);
}
