package com.jejoonlee.movmag.app.elasticsearch.service.impl;

import com.jejoonlee.movmag.app.elasticsearch.document.ReviewDocument;
import com.jejoonlee.movmag.app.elasticsearch.repository.ReviewSearchRepository;
import com.jejoonlee.movmag.app.elasticsearch.service.ReviewSearchService;
import com.jejoonlee.movmag.app.review.dto.ReviewDetail;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewSearchServiceImpl implements ReviewSearchService {

    private final ReviewSearchRepository reviewSearchRepository;

    @Override
    public boolean saveToReviewDocument(ReviewDetail reviewDetail) {

        log.info("{}", reviewDetail);

        reviewSearchRepository.save(ReviewDocument.fromReviewDetail(reviewDetail));

        return true;
    }

    @Override
    public void updateFromReviewDocument(ReviewDetail reviewDetail) {

    }

    @Override
    public void deleteFromReviewDocument(ReviewDetail reviewDetail) {

    }
}
