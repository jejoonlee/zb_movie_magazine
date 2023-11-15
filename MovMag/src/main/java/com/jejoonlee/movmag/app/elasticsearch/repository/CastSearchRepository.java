package com.jejoonlee.movmag.app.elasticsearch.repository;

import com.jejoonlee.movmag.app.elasticsearch.document.CastDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface CastSearchRepository extends ElasticsearchRepository<CastDocument, Long> {
        Page<CastDocument> findAllByNameEng(String name, Pageable pageable);
}
