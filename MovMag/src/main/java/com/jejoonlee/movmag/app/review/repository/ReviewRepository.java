package com.jejoonlee.movmag.app.review.repository;

import com.jejoonlee.movmag.app.movie.domain.MovieEntity;
import com.jejoonlee.movmag.app.review.domain.ReviewEntity;
import com.jejoonlee.movmag.app.review.repository.response.MovieScore;
import com.jejoonlee.movmag.app.review.repository.response.PopularReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {

    List<MovieScore> findMovieScoreByMovieEntity(MovieEntity movieEntity);

    @Query(value="select review_entity_review_id as review, count(review_entity_review_id) as count " +
            "from review_like group by review order by count DESC limit 10", nativeQuery = true)
    List<PopularReview> findTop10PopularReviewByReviewLike();

}