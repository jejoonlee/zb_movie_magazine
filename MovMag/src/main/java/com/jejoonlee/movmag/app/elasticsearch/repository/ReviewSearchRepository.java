package com.jejoonlee.movmag.app.elasticsearch.repository;

import com.jejoonlee.movmag.app.elasticsearch.document.ReviewDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ReviewSearchRepository extends ElasticsearchRepository<ReviewDocument, Long> {
}
