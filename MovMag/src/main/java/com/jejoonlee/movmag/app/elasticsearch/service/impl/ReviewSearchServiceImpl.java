package com.jejoonlee.movmag.app.elasticsearch.service.impl;

import com.jejoonlee.movmag.app.elasticsearch.document.ReviewDocument;
import com.jejoonlee.movmag.app.elasticsearch.repository.ReviewSearchRepository;
import com.jejoonlee.movmag.app.elasticsearch.service.ReviewSearchService;
import com.jejoonlee.movmag.app.review.dto.ReviewDetail;
import com.jejoonlee.movmag.app.review.repository.ReviewRepository;
import com.jejoonlee.movmag.exception.ReviewException;
import com.jejoonlee.movmag.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewSearchServiceImpl implements ReviewSearchService {

    private final ReviewSearchRepository reviewSearchRepository;
    private final ReviewRepository reviewRepository;

    @Override
    public boolean saveToReviewDocument(ReviewDetail reviewDetail) {

        try{
            reviewSearchRepository.save(ReviewDocument.fromReviewDetail(reviewDetail));
        } catch(Exception e) {
            reviewRepository.delete(reviewRepository.findById(reviewDetail.getReviewId())
                    .orElseThrow(() -> new ReviewException(ErrorCode.ELASTICSEARCH_CAN_NOT_FIND_REVIEW)));
            throw new RuntimeException();
        }

        return true;
    }

    @Override
    public void updateFromReviewDocument(ReviewDetail reviewDetail) {
        ReviewDocument reviewDocument = reviewSearchRepository.findById(reviewDetail.getReviewId())
                .orElseThrow(() -> new ReviewException(ErrorCode.ELASTICSEARCH_CAN_NOT_FIND_REVIEW));

        reviewDocument = ReviewDocument.updateReviewDetail(reviewDocument, reviewDetail);

        reviewSearchRepository.save(reviewDocument);
    }

    @Override
    public void deleteFromReviewDocument(ReviewDetail reviewDetail) {
        ReviewDocument reviewDocument = ReviewDocument.fromReviewDetail(reviewDetail);
        reviewSearchRepository.delete(reviewDocument);
    }
}
