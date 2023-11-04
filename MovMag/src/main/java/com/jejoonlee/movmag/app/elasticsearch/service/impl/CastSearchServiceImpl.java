package com.jejoonlee.movmag.app.elasticsearch.service.impl;

import com.jejoonlee.movmag.app.elasticsearch.document.CastDocument;
import com.jejoonlee.movmag.app.elasticsearch.repository.CastSearchRepository;
import com.jejoonlee.movmag.app.elasticsearch.service.CastSearchService;
import com.jejoonlee.movmag.app.movie.repository.CastRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CastSearchServiceImpl implements CastSearchService {

    private final CastSearchRepository castSearchRepository;
    private final CastRepository castRepository;

    @Override
    public Long saveAllCastsToCastDocument() {

        Long dataCount = (Long) castRepository.count();
        Long pages = dataCount / 1000;

        for(int i = 0; i <= pages; i++) {
            List<CastDocument> listCastDocument =
                    castRepository.findAll(PageRequest.of(i, 1000))
                            .stream()
                            .map(CastDocument::fromEntity)
                            .collect(Collectors.toList());

            castSearchRepository.saveAll(listCastDocument);
        }

        return dataCount;
    }

    @Override
    public List<CastDocument> searchByCast(String name) {

        return castSearchRepository.findAllByNameEng(name);
    }
}
