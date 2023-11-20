package com.jejoonlee.movmag.app.elasticsearch.service.impl;

import com.jejoonlee.movmag.app.elasticsearch.document.CastDocument;
import com.jejoonlee.movmag.app.elasticsearch.dto.CastElsDto;
import com.jejoonlee.movmag.app.elasticsearch.repository.CastSearchRepository;
import com.jejoonlee.movmag.app.elasticsearch.service.CastSearchService;
import com.jejoonlee.movmag.app.movie.domain.MovieEntity;
import com.jejoonlee.movmag.app.movie.dto.MovieDto;
import com.jejoonlee.movmag.app.movie.repository.CastRepository;
import com.jejoonlee.movmag.app.movie.repository.MovieRepository;
import com.jejoonlee.movmag.exception.ErrorCode;
import com.jejoonlee.movmag.exception.MovieException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CastSearchServiceImpl implements CastSearchService {

    private final CastSearchRepository castSearchRepository;
    private final CastRepository castRepository;
    private final MovieRepository movieRepository;

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

    private CastElsDto.Page getMovieByCast(String name, int page) {

        Page<CastDocument> castDocumentPage =
                castSearchRepository.findAllByNameEng(name, PageRequest.of(page, 10));

        List<CastElsDto.Response> result = new ArrayList<>();

        List<CastDocument> castDocumentList = castDocumentPage.getContent();

        if (page <= 0 && page > castDocumentPage.getTotalPages())
            throw new MovieException(ErrorCode.PAGE_NOT_FOUND);

        if (castDocumentList.size() == 0)
            throw new MovieException(ErrorCode.CAST_NOT_FOUND);

        for (int i = 0; i < castDocumentList.size(); i++) {

            CastDocument castDocument = castDocumentList.get(i);

            CastElsDto.Response castElsDto = CastElsDto.Response.fromDocument(castDocument);

            List<Integer> movieIds = (ArrayList<Integer>) castDocument.getMovieId().get("movieId");

            List<CastElsDto.Movie> featuredMovies = new ArrayList<>();

            for (int num : movieIds) {
                Long movieId = Long.valueOf(num);

                Optional<MovieEntity> movieEntity = movieRepository.findById(movieId);

                if (movieEntity.isPresent()) {
                    MovieDto movieDto = MovieDto.fromEntity(movieEntity.get());
                    CastElsDto.Movie castMovie = CastElsDto.Movie.fromMovieDto(movieDto);
                    featuredMovies.add(castMovie);
                }
            }

            castElsDto.setFeaturedMovies(featuredMovies);

            result.add(castElsDto);
        }

        return CastElsDto.Page.builder()
                .page(page)
                .totalPage(castDocumentPage.getTotalPages())
                .foundDataNum(castDocumentPage.getTotalElements())
                .data(result)
                .build();
    }

    @Override
    public CastElsDto.Page searchByCast(String name, int page) {
        return getMovieByCast(name, page);
    }
}
