package com.jejoonlee.movmag.app.elasticsearch.repository;

import com.jejoonlee.movmag.app.elasticsearch.document.ReviewDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface ReviewSearchRepository extends ElasticsearchRepository<ReviewDocument, Long> {

    Page<ReviewDocument> findAllByAuthorOrderByUpdatedAtDesc(String author, Pageable pageable);

    List<ReviewDocument> findAllByAuthorOrderByUpdatedAtDesc(String author);

    Page<ReviewDocument> findAllByReviewTitleOrderByUpdatedAtDesc(String reviewTitle, Pageable pageable);

    Page<ReviewDocument> findAllByMovieTitleEngOrderByUpdatedAtDesc(String movieTitleEng, Pageable pageable);

    Page<ReviewDocument> findAllByMovieTitleKorOrderByUpdatedAtDesc(String movieTitleKor, Pageable pageable);

}
