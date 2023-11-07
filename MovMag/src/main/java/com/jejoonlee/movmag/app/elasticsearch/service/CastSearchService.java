package com.jejoonlee.movmag.app.elasticsearch.service;

import com.jejoonlee.movmag.app.elasticsearch.dto.CastElsDto;

import java.util.List;

public interface CastSearchService {

    Long saveAllCastsToCastDocument();

    List<CastElsDto.Response> searchByCast(String name);
}
