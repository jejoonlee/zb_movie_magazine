package com.jejoonlee.movmag.app.elasticsearch.repository;

import com.jejoonlee.movmag.app.elasticsearch.document.MovieDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface MovieSearchRepository extends ElasticsearchRepository<MovieDocument, Long> {

    List<MovieDocument> findAllByTitleEng(String movieTitle);

    List<MovieDocument> findAllByTitleKor(String movieTitle);

}
