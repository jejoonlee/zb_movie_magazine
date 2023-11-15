package com.jejoonlee.movmag.app.elasticsearch.repository;

import com.jejoonlee.movmag.app.elasticsearch.document.MovieDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface MovieSearchRepository extends ElasticsearchRepository<MovieDocument, Long> {

    Page<MovieDocument> findAllByTitleEngOrderByReleasedDateDesc(String movieTitle, Pageable pageable);

    Page<MovieDocument> findAllByTitleKorOrderByReleasedDateDesc(String movieTitle, Pageable pageable);

}
