package com.jejoonlee.movmag.app.review.service.impl;

import com.jejoonlee.movmag.app.elasticsearch.service.ReviewSearchService;
import com.jejoonlee.movmag.app.member.domain.MemberEntity;
import com.jejoonlee.movmag.app.member.domain.MemberRole;
import com.jejoonlee.movmag.app.member.dto.MemberDto;
import com.jejoonlee.movmag.app.movie.domain.MovieEntity;
import com.jejoonlee.movmag.app.movie.repository.MovieRepository;
import com.jejoonlee.movmag.app.review.domain.LikeEntity;
import com.jejoonlee.movmag.app.review.domain.ReviewEntity;
import com.jejoonlee.movmag.app.review.dto.*;
import com.jejoonlee.movmag.app.review.repository.ReviewLikeRepository;
import com.jejoonlee.movmag.app.review.repository.ReviewRepository;
import com.jejoonlee.movmag.app.review.repository.response.MovieScore;
import com.jejoonlee.movmag.app.review.repository.response.PopularReview;
import com.jejoonlee.movmag.app.review.service.ReviewService;
import com.jejoonlee.movmag.app.review.type.LikeOrCancel;
import com.jejoonlee.movmag.exception.ErrorCode;
import com.jejoonlee.movmag.exception.MovieException;
import com.jejoonlee.movmag.exception.ReviewClientException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final MovieRepository movieRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewLikeRepository reviewLikeRepository;
    private final ReviewSearchService reviewSearchService;


    private MovieEntity getMovieEntity(Long movieId){
        return movieRepository.findById(movieId)
                .orElseThrow(() -> new MovieException(ErrorCode.MOVIE_NOT_FOUND));
    }

    private ReviewEntity getReviewEntity(Long reviewId){
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewClientException(ErrorCode.REVIEW_NOT_FOUND));
    }

    private double getMovieScoreAvg(MovieEntity movie){

        List<MovieScore> movieScores = reviewRepository.findMovieScoreByMovieEntity(movie);

        Double totalScore = movieScores.stream().mapToDouble(MovieScore::getMovieScore).sum();

        return Math.round((totalScore / Long.valueOf(movieScores.size())) * 100) / 100.0;
    }

    private ReviewEntity checkAvailabilityToChangeReview(MemberDto currentUser, Long reviewId){

        ReviewEntity review = getReviewEntity(reviewId);

        // 모든 유저는 글 수정을 할 수 없다
        // Editor는 자신이 쓴 글만 수정할 수 있다
        // Admin은 모든 글을 수정할 수 있다
        if (currentUser.getRole().equals(MemberRole.EDITOR.getValue()) &&
                currentUser.getMemberId() != review.getMemberEntity().getMemberId()) {
            throw new ReviewClientException(ErrorCode.LOGGED_IN_USER_AND_AUTHOR_UNMATCH);
        }

        return review;
    }

    @Override
    public ReviewRegister.Response createReview(ReviewRegister.Request request, Authentication authentication) {

        MemberDto currentUser = (MemberDto) authentication.getPrincipal();

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

        // 엘라스틱 서치에 저장
        reviewSearchService.saveToReviewDocument(ReviewDetail.fromEntity(reviewEntity));
        log.info("리뷰 정보 엘라스틱 서치에 저장 성공");

        // 해당 영화에 대한 리뷰에서 평가한 점수를 평균 점수로 만들어서
        // 가져온 영화 entity 점수에 저장
        movie.setMovieScore(getMovieScoreAvg(movie));

        movieRepository.save(movie);

        return ReviewRegister.Response.fromEntity(reviewEntity);
    }

    @Override
    public ReviewDetail getReviewDetail(Long reviewId, Authentication authentication) {

        // 리뷰가 존재하는지 확인
        ReviewEntity reviewEntity = getReviewEntity(reviewId);

        return ReviewDetail.fromEntity(reviewEntity);
    }

    @Override
    public ReviewRegister.Response updateReview(ReviewRegister.Update update, Authentication authentication) {

        MemberDto currentUser = (MemberDto) authentication.getPrincipal();

        // 입력한 리뷰ID가 유효한지 확인 +
        // 로그인한 사람이 현재 리뷰를 수정할 수 있는 권한이 있는지 확인
        ReviewEntity review = checkAvailabilityToChangeReview(currentUser, update.getReviewId());

        MovieEntity movie = movieRepository.findById(update.getMovieId())
                .orElseThrow(()->new MovieException(ErrorCode.MOVIE_NOT_FOUND));

        review = ReviewEntity.updateEntity(review, movie, update);

        reviewRepository.save(review);

        reviewSearchService.updateFromReviewDocument(ReviewDetail.fromEntity(review));

        movie.setMovieScore(getMovieScoreAvg(movie));
        movieRepository.save(movie);

        return ReviewRegister.Response.fromEntity(review);
    }

    @Override
    public ReviewDelete.Response deleteReview(Long reviewId, Authentication authentication) {

        // 유저가 존재하는지 확인
        MemberDto member = (MemberDto) authentication.getPrincipal();

        // 입력한 리뷰ID가 유효한지 확인 +
        // 로그인한 사람이 현재 리뷰를 수정할 수 있는 권한이 있는지 확인
        ReviewEntity reviewEntity = checkAvailabilityToChangeReview(member, reviewId);

        // 엘라스틱 서치에서 먼저 삭제를 하기
        reviewSearchService.deleteFromReviewDocument(ReviewDetail.fromEntity(reviewEntity));

        reviewRepository.delete(reviewEntity);

        return ReviewDelete.Response.builder()
                .reviewID(reviewId)
                .author(reviewEntity.getMemberEntity().getUsername())
                .message("정상적으로 삭제를 했습니다")
                .build();
    }

    // ================================== 새로 추가 ==================================

    private LikeOrCancel likeOrCancelLike(ReviewEntity reviewEntity, MemberEntity memberEntity) {

        LikeEntity likeEntity = reviewLikeRepository.findByReviewEntityAndMemberEntity(reviewEntity, memberEntity);

        // 현재 로그인 한 사람이 리뷰에 좋아요를 안 누른 상태면, LikeEntity에 저장한다
        // else 현재 로그인 한 사람이 이미 리뷰에 좋아요를 눌렀다면, LikeEntity에서 좋아요를 삭제한다

        if (likeEntity == null) {
            likeEntity = ReviewLikeDto.toEntity(ReviewLikeDto.builder()
                    .reviewEntity(reviewEntity)
                    .memberEntity(memberEntity)
                    .build());

            reviewLikeRepository.save(likeEntity);

            return LikeOrCancel.ClickLike;
        } else {
            reviewLikeRepository.delete(likeEntity);
            return LikeOrCancel.CancelLIke;
        }
    }

    @Override
    public ReviewLikeResponse likeReview(Long reviewId, Authentication authentication) {

        // Param에 불러온 리뷰가 존재하는지 확인한다
        ReviewEntity reviewEntity = getReviewEntity(reviewId);

        // MemeberEntity 가지고 오기
        MemberEntity memberEntity = MemberDto.toEntity((MemberDto) authentication.getPrincipal());

        LikeOrCancel shouldLikeOrCancel = likeOrCancelLike(reviewEntity, memberEntity);

        if (shouldLikeOrCancel == LikeOrCancel.ClickLike) {
            return ReviewLikeResponse.builder()
                    .message("좋아요를 눌렀습니다")
                    .build();
        } else {
            return ReviewLikeResponse.builder()
                    .message("좋아요를 취소했습니다")
                    .build();
        }
    }

    private List<ReviewPopular> getPopularReviewResult(){

        List<PopularReview> popularReviews = reviewRepository.findTop10PopularReviewByReviewLike();

        return popularReviews.stream()
                .map(pr -> new ReviewPopular(pr.getCount(),
                        ReviewRegister.Response.fromEntity(reviewRepository.findById(pr.getReview()).orElse(null))))
                .collect(Collectors.toList());
    }

    @Override
    public List<ReviewPopular> getPopularReviews() {
        return getPopularReviewResult();
    }
}
