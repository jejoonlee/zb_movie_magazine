package com.jejoonlee.movmag.app.review.service.impl;

import com.jejoonlee.movmag.app.member.dto.MemberDto;
import com.jejoonlee.movmag.app.movie.domain.MovieEntity;
import com.jejoonlee.movmag.app.movie.repository.MovieRepository;
import com.jejoonlee.movmag.app.review.domain.ReviewEntity;
import com.jejoonlee.movmag.app.review.dto.ReviewDelete;
import com.jejoonlee.movmag.app.review.dto.ReviewDetail;
import com.jejoonlee.movmag.app.review.dto.ReviewDto;
import com.jejoonlee.movmag.app.review.dto.ReviewRegister;
import com.jejoonlee.movmag.app.review.repository.ReviewRepository;
import com.jejoonlee.movmag.app.review.service.ReviewService;
import com.jejoonlee.movmag.exception.ErrorCode;
import com.jejoonlee.movmag.exception.MemberException;
import com.jejoonlee.movmag.exception.MovieException;
import com.jejoonlee.movmag.exception.ReviewException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final MovieRepository movieRepository;
    private final ReviewRepository reviewRepository;

    private MemberDto authenticated(Authentication authentication) {
        if (!authentication.isAuthenticated())
            throw new MemberException(ErrorCode.USER_NOT_LOGGED_IN);

        return (MemberDto) authentication.getPrincipal();
    }

    private MovieEntity getMovieEntity(Long movieId){
        return movieRepository.findById(movieId)
                .orElseThrow(() -> new MovieException(ErrorCode.MOVIE_NOT_FOUND));
    }

    private ReviewEntity getReviewEntity(Long reviewId){
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewException(ErrorCode.REVIEW_NOT_FOUND));
    }

    private double updateMovieScore(MovieEntity movie){

        List<ReviewEntity> review = reviewRepository.findAllByMovieEntity(movie);

        Double totalScore = review.stream()
                .mapToDouble(ReviewEntity::getMovieScore)
                .sum();

        return Math.round((totalScore / Long.valueOf(review.size())) * 100) / 100.0;
    }

    private ReviewEntity checkAvailability(MemberDto currentUser, Long reviewId){

        ReviewEntity review = getReviewEntity(reviewId);

        // 모든 유저는 글 수정을 할 수 없다
        // Editor는 자신이 쓴 글만 수정할 수 있다
        // Admin은 모든 글을 수정할 수 있다
        if (currentUser.getRole().equals("User")) {
            throw new MemberException(ErrorCode.USER_PERMISSION_NOT_GRANTED);

        } else if (currentUser.getRole().equals("Editor")) {

            if (currentUser.getMemberId() != review.getMemberEntity().getMemberId()) {
                throw new ReviewException(ErrorCode.LOGGED_IN_USER_AND_AUTHOR_UNMATCH);
            }
        }

        return review;
    }

    @Override
    public ReviewRegister.Response createReview(ReviewRegister.Request request, Authentication authentication) {

        // 혹시 모르니 로그인 확인 (다시 한번)
        MemberDto currentUser = authenticated(authentication);

        // 영화ID를 통해 영화를 찾고, 영화가 있으면 Entity 객체 생성
        Long movieId = request.getMovieId();

        MovieEntity movie = getMovieEntity(movieId);

        // reviewDto 객체 생성해서 reviewEntity 객체 만들기
        ReviewDto reviewDto = ReviewDto.builder()
                .movieEntity(movie)
                .memberEntity(MemberDto.toEntity(currentUser))
                .reviewTitle(request.getReviewTitle())
                .reviewOneline(request.getReviewOneline())
                .movieScore(request.getMovieScore())
                .content(request.getContent())
                .registeredAt(LocalDateTime.now())
                .updatedAt(null)
                .build();

        ReviewEntity reviewEntity = reviewDto.toEntity();

        // 해당 리뷰 저장
        reviewRepository.save(reviewEntity);

        // 해당 영화에 대한 리뷰에서 평가한 점수를 평균 점수로 만들어서
        // 가져온 영화 entity 점수에 저장
        movie.setMovieScore(updateMovieScore(movie));
        movieRepository.save(movie);

        return ReviewRegister.Response.fromEntity(reviewEntity);
    }

    @Override
    public ReviewDetail getReviewDetail(Long reviewId, Authentication authentication) {

        // 로그인 확인
        authenticated(authentication);

        // 리뷰가 존재하는지 확인
        ReviewEntity reviewEntity = getReviewEntity(reviewId);

        String movieTitleEng = reviewEntity.getMovieEntity().getTitleEng();
        String movieTitleKor = reviewEntity.getMovieEntity().getTitleKor();
        String author = reviewEntity.getMemberEntity().getUsername();

        return ReviewDetail.builder()
                .reviewId(reviewEntity.getReviewId())
                .movieTitleEng(movieTitleEng)
                .movieTitleKor(movieTitleKor)
                .movieScore(reviewEntity.getMovieScore())
                .author(author)
                .reviewTitle(reviewEntity.getReviewTitle())
                .reviewOneline(reviewEntity.getReviewOneline())
                .content(reviewEntity.getContent())
                .registeredAt(reviewEntity.getRegisteredAt())
                .updatedAt(reviewEntity.getUpdatedAt())
                .build();
    }

    @Override
    public ReviewRegister.Response updateReview(ReviewRegister.Update update, Authentication authentication) {

        MemberDto currentUser = authenticated(authentication);

        // 입력한 리뷰ID가 유효한지 확인 +
        // 로그인한 사람이 현재 리뷰를 수정할 수 있는 권한이 있는지 확인
        ReviewEntity review = checkAvailability(currentUser, update.getReviewId());

        MovieEntity movie = movieRepository.findById(update.getMovieId())
                .orElseThrow(()->new MovieException(ErrorCode.MOVIE_NOT_FOUND));

        review.setMovieEntity(movie);
        review.setReviewTitle(update.getReviewTitle());
        review.setReviewOneline(update.getReviewOneline());
        review.setMovieScore(update.getMovieScore());
        review.setContent(update.getContent());

        reviewRepository.save(review);

        movie.setMovieScore(updateMovieScore(movie));
        movieRepository.save(movie);

        return ReviewRegister.Response.fromEntity(review);
    }

    @Override
    public ReviewDelete.Response deleteReview(Long reviewId, Authentication authentication) {

        // 유저가 존재하는지 확인
        MemberDto member = authenticated(authentication);

        // 입력한 리뷰ID가 유효한지 확인 +
        // 로그인한 사람이 현재 리뷰를 수정할 수 있는 권한이 있는지 확인
        ReviewEntity reviewEntity = checkAvailability(member, reviewId);

        reviewRepository.delete(reviewEntity);

        return ReviewDelete.Response.builder()
                .reviewID(reviewId)
                .author(reviewEntity.getMemberEntity().getUsername())
                .message("정상적으로 삭제를 했습니다")
                .build();
    }
}
