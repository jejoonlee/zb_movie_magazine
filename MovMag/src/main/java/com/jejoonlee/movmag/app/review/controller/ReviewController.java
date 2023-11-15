package com.jejoonlee.movmag.app.review.controller;

import com.jejoonlee.movmag.app.review.dto.*;
import com.jejoonlee.movmag.app.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;



    // 리뷰 작성
    // http://localhost:8080/review/create
    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EDITOR')")
    public ReviewRegister.Response createReview(
            @RequestBody @Valid ReviewRegister.Request request,
            Authentication authentication
    ){
        return reviewService.createReview(request, authentication);
    }

    // 리뷰 보기
    // http://localhost:8080/review?reviewId={review_id}
    @GetMapping("")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EDITOR', 'ROLE_USER')")
    public ReviewDetail getReviewDetail(
            @RequestParam Long reviewId,
            Authentication authentication
    ) {
        return reviewService.getReviewDetail(reviewId, authentication);
    }

    // 리뷰 수정
    // http://localhost:8080/review/update
    @PutMapping("/update")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EDITOR')")
    public ReviewRegister.Response updateReview(
            @RequestBody @Valid ReviewRegister.Update update,
            Authentication authentication
    ) {
        return reviewService.updateReview(update, authentication);
    }

    // 리뷰 삭제
    // http://localhost:8080/review/delete
    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EDITOR')")
    public ReviewDelete.Response deleteReview(
            @RequestParam Long reviewId,
            Authentication authentication
    ) {
        return reviewService.deleteReview(reviewId, authentication);
    }

    // ================================== 새로 추가 ==================================

    // 리뷰 좋아요 or 취소
    // http://localhost:8080/review/like/{review_id}/{user_id}
    @GetMapping("/like")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EDITOR', 'ROLE_USER')")
    public ReviewLikeResponse likeReview(
            @RequestParam Long reviewId,
            Authentication authentication
    ) {
        return reviewService.likeReview(reviewId, authentication);
    }

    // 인기 리뷰
    // http://localhost:8080/review/popular
    @GetMapping("/popular")
    public List<ReviewPopular> getPopular(
    ) {
        return reviewService.getPopularReviews();
    }


    // 리뷰 찾기, 리뷰를 쓴 유저 (elasticsearch)
    // http://localhost:8080/review?user={keyword}

    // 리뷰 제목 (elasticsearch)
    // http://localhost:8080/review?reviewName={review_name}

    // 영화  (elasticsearch)
    // http://localhost:8080/review?movieName={movie_name}


}
