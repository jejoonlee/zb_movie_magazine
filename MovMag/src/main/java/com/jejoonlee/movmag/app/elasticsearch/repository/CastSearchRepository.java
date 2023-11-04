package com.jejoonlee.movmag.app.elasticsearch.repository;

import com.jejoonlee.movmag.app.elasticsearch.document.CastDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface CastSearchRepository extends ElasticsearchRepository<CastDocument, Long> {

        List<CastDocument> findAllByNameEng(String name);
}
