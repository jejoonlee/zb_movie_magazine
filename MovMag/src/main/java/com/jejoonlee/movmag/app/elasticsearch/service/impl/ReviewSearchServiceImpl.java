package com.jejoonlee.movmag.app.elasticsearch.service.impl;

import com.jejoonlee.movmag.app.elasticsearch.document.ReviewDocument;
import com.jejoonlee.movmag.app.elasticsearch.repository.ReviewSearchRepository;
import com.jejoonlee.movmag.app.elasticsearch.service.ReviewSearchService;
import com.jejoonlee.movmag.exception.ErrorCode;
import com.jejoonlee.movmag.app.review.dto.ReviewDetail;
import com.jejoonlee.movmag.exception.ReviewException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewSearchServiceImpl implements ReviewSearchService {

    private final ReviewSearchRepository reviewSearchRepository;

    @Override
    public boolean saveToReviewDocument(ReviewDetail reviewDetail) {

        try {
            reviewSearchRepository.save(ReviewDocument.fromReviewDetail(reviewDetail));
        } catch(Exception e) {
            throw new ReviewException(ErrorCode.SAVE_TO_ELASTICSEARCH_UNSUCCESSFUL);
        }

        return true;
    }

    @Override
    public void updateFromReviewDocument(ReviewDetail reviewDetail) {

    }

    @Override
    public void deleteFromReviewDocument(ReviewDetail reviewDetail) {

    }
}
