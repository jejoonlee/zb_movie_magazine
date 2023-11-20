package com.jejoonlee.movmag.app.elasticsearch.service;

import com.jejoonlee.movmag.app.elasticsearch.dto.ReviewElsDto;
import com.jejoonlee.movmag.app.review.dto.ReviewDetail;

public interface ReviewSearchService {

    boolean saveToReviewDocument(ReviewDetail reviewDetail);

    void updateFromReviewDocument(ReviewDetail reviewDetail);

    void deleteFromReviewDocument(ReviewDetail reviewDetail);

    ReviewElsDto.PageInfo searchReviewByAuthor(String author, int page);

    ReviewElsDto.PageInfo searchReviewByReviewTitle(String reviewTitle, int page);

    ReviewElsDto.PageInfo searchReviewByMovieTitle(String movieTitle, String lang, int page);
}
