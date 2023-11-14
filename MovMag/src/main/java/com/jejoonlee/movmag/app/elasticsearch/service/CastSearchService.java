package com.jejoonlee.movmag.app.elasticsearch.service;

import com.jejoonlee.movmag.app.elasticsearch.dto.CastElsDto;

public interface CastSearchService {

    Long saveAllCastsToCastDocument();

    CastElsDto.Page searchByCast(String name, int page);
}
