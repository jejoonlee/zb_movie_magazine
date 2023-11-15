package com.jejoonlee.movmag.app.elasticsearch.service;

import com.jejoonlee.movmag.app.review.dto.ReviewDetail;

public interface ReviewSearchService {

    boolean saveToReviewDocument(ReviewDetail reviewDetail);

    void updateFromReviewDocument(ReviewDetail reviewDetail);

    void deleteFromReviewDocument(ReviewDetail reviewDetail);
}
