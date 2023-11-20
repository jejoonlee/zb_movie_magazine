package com.jejoonlee.movmag.app.elasticsearch.service.impl;

import com.jejoonlee.movmag.app.elasticsearch.document.ReviewDocument;
import com.jejoonlee.movmag.app.elasticsearch.dto.ReviewElsDto;
import com.jejoonlee.movmag.app.elasticsearch.repository.ReviewSearchRepository;
import com.jejoonlee.movmag.app.elasticsearch.service.ReviewSearchService;
import com.jejoonlee.movmag.app.review.dto.ReviewDetail;
import com.jejoonlee.movmag.app.review.repository.ReviewRepository;
import com.jejoonlee.movmag.exception.ErrorCode;
import com.jejoonlee.movmag.exception.ReviewClientException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
                    .orElseThrow(() -> new ReviewClientException(ErrorCode.ELASTICSEARCH_CAN_NOT_FIND_REVIEW)));
            throw new RuntimeException();
        }

        return true;
    }

    @Override
    public void updateFromReviewDocument(ReviewDetail reviewDetail) {
        ReviewDocument reviewDocument = reviewSearchRepository.findById(reviewDetail.getReviewId())
                .orElseThrow(() -> new ReviewClientException(ErrorCode.ELASTICSEARCH_CAN_NOT_FIND_REVIEW));

        reviewDocument = ReviewDocument.updateReviewDetail(reviewDocument, reviewDetail);

        reviewSearchRepository.save(reviewDocument);
    }

    @Override
    public void deleteFromReviewDocument(ReviewDetail reviewDetail) {
        ReviewDocument reviewDocument = ReviewDocument.fromReviewDetail(reviewDetail);
        reviewSearchRepository.delete(reviewDocument);
    }


    // ============ 검색 ===============

    private ReviewElsDto.PageInfo getSearchResult(Page<ReviewDocument> reviewDocumentPage, int page) {
        if (page < 0 && page > reviewDocumentPage.getTotalPages())
            throw new ReviewClientException(ErrorCode.PAGE_NOT_FOUND);

        List<ReviewElsDto.Response> reviewResults = reviewDocumentPage.getContent()
                .stream()
                .map(ReviewElsDto.Response :: fromDocument)
                .collect(Collectors.toList());

        return ReviewElsDto.PageInfo.builder()
                .foundDataNum(reviewDocumentPage.getTotalElements())
                .totalPage(reviewDocumentPage.getTotalPages())
                .page(page)
                .data(reviewResults)
                .build();
    }

    private ReviewElsDto.PageInfo getReviewListByAuthor(String author, int page) {

        Page<ReviewDocument> reviewDocumentPage = reviewSearchRepository
                .findAllByAuthorOrderByUpdatedAtDesc(author, PageRequest.of(page, 20));

        return getSearchResult(reviewDocumentPage, page);
    }

    @Override
    public ReviewElsDto.PageInfo searchReviewByAuthor(String author, int page) {
        return getReviewListByAuthor(author, page);
    }

    private ReviewElsDto.PageInfo getReviewListByReviewTitle(String reviewTitle, int page) {
        Page<ReviewDocument> reviewDocumentPage = reviewSearchRepository
                .findAllByReviewTitleOrderByUpdatedAtDesc(reviewTitle, PageRequest.of(page, 20));

        return getSearchResult(reviewDocumentPage, page);
    }

    @Override
    public ReviewElsDto.PageInfo searchReviewByReviewTitle(String reviewTitle, int page) {
        return getReviewListByReviewTitle(reviewTitle, page);
    }

    private ReviewElsDto.PageInfo getReviewListByMovieTitle(String movieTitle, String lang, int page) {
        lang = lang.toLowerCase();

        Page<ReviewDocument> reviewDocumentPage;

        if (lang.equals("korean")) {
            reviewDocumentPage = reviewSearchRepository
                    .findAllByMovieTitleKorOrderByUpdatedAtDesc(movieTitle, PageRequest.of(page, 20));
        } else if (lang.equals("english")) {
            reviewDocumentPage = reviewSearchRepository
                    .findAllByMovieTitleEngOrderByUpdatedAtDesc(movieTitle, PageRequest.of(page, 20));
        } else {
            throw new ReviewClientException(ErrorCode.WRONG_LANGUAGE);
        }

        return getSearchResult(reviewDocumentPage, page);
    }

    @Override
    public ReviewElsDto.PageInfo searchReviewByMovieTitle(String movieTitle, String lang, int page) {
        return getReviewListByMovieTitle(movieTitle, lang, page);
    }
}
