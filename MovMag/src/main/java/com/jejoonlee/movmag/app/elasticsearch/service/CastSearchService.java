package com.jejoonlee.movmag.app.elasticsearch.service;

import com.jejoonlee.movmag.app.elasticsearch.document.CastDocument;

import java.util.List;

public interface CastSearchService {

    Long saveAllCastsToCastDocument();

    List<CastDocument> searchByCast(String name);
}
