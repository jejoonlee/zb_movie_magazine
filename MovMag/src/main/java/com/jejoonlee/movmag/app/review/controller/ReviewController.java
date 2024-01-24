package com.jejoonlee.movmag.app.review.controller;

import com.jejoonlee.movmag.app.elasticsearch.dto.ReviewElsDto;
import com.jejoonlee.movmag.app.elasticsearch.service.ReviewSearchService;
import com.jejoonlee.movmag.app.review.dto.*;
import com.jejoonlee.movmag.app.review.service.ReviewService;
import io.swagger.annotations.ApiOperation;
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
    private final ReviewSearchService reviewSearchService;

    // 리뷰 작성
    // http://localhost:8080/review/create
    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EDITOR')")
    @ApiOperation(value="영화에 대한 리뷰 작성. Admin과 Editor만 가능")
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
    @ApiOperation(value="리뷰에 대한 상세 내용 확인. 로그인을 해야 한다")
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
    @ApiOperation(value="리뷰를 수정한다. 리뷰를 쓴 사람 또는 Admin만 수정 가능")
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
    @ApiOperation(value="리뷰를 삭제한다. 리뷰를 쓴 사람 또는 Admin이 부적절하다고 판단할 때에 삭제가 가능하다")
    public ReviewDelete.Response deleteReview(
            @RequestParam Long reviewId,
            Authentication authentication
    ) {
        return reviewService.deleteReview(reviewId, authentication);
    }

    // 리뷰 좋아요 or 취소
    // http://localhost:8080/review/like/{review_id}
    @GetMapping("/like")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EDITOR', 'ROLE_USER')")
    @ApiOperation(value="리뷰 좋아요를 누르거나, 좋아요 취소를 할 수 있다. 로그인을 해야 한다")
    public ReviewLikeResponse likeReview(
            @RequestParam Long reviewId,
            Authentication authentication
    ) {
        return reviewService.likeReview(reviewId, authentication);
    }

    // 인기 리뷰
    // http://localhost:8080/review/popular
    @GetMapping("/popular")
    @ApiOperation(value="리뷰 중에, 제일 많이 좋아요가 눌린 10개의 리뷰를 보여준다.")
    public List<ReviewPopular> getPopular(
    ) {
        return reviewService.getPopularReviews();
    }


    // 리뷰 찾기, 리뷰를 쓴 유저 (elasticsearch)
    // http://localhost:8080/review/search/author?author={keyword}&page={page}
    @GetMapping("/search/author")
    @ApiOperation(value="리뷰를 작성한 사람의 이름을 기반으로 리뷰를 찾는다")
    public ReviewElsDto.PageInfo searchReviewByAuthor(
            @RequestParam String name,
            @RequestParam int page
    ) {
        return reviewSearchService.searchReviewByAuthor(name, page);
    }

    // 리뷰 제목 (elasticsearch)
    // http://localhost:8080/review/search/review?reviewTitle={review_name}&page={페이지}
    @GetMapping("/search/review")
    @ApiOperation(value="리뷰의 제목을 기반으로 검색하여 리뷰를 찾는 기능이다")
    public ReviewElsDto.PageInfo searchReviewByReviewTitle(
            @RequestParam String title,
            @RequestParam int page
    ) {
        return reviewSearchService.searchReviewByReviewTitle(title, page);
    }

    // 영화  (elasticsearch)
    // http://localhost:8080/review/search/movie?movieTitle={movie_name}&language={언어}&page={페이지}
    // 언어는 korean 또는 english
    @GetMapping("/search/movie")
    @ApiOperation(value="영화의 제목을 기반으로 검색하여 리뷰를 찾는 기능이다")
    public ReviewElsDto.PageInfo searchReviewByMovieTitle(
            @RequestParam String language,
            @RequestParam String title,
            @RequestParam int page
    ) {
        return reviewSearchService.searchReviewByMovieTitle(title, language, page);
    }

}
