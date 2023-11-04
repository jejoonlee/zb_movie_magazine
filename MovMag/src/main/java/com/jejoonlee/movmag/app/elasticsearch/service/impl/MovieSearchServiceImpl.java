package com.jejoonlee.movmag.app.elasticsearch.service.impl;

import com.jejoonlee.movmag.app.elasticsearch.document.MovieDocument;
import com.jejoonlee.movmag.app.elasticsearch.repository.MovieSearchRepository;
import com.jejoonlee.movmag.app.elasticsearch.service.MovieSearchService;
import com.jejoonlee.movmag.app.movie.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MovieSearchServiceImpl implements MovieSearchService {

    private final MovieRepository movieRepository;
    private final MovieSearchRepository movieSearchRepository;

    @Override
    public Long saveAllMoviesToMovieDocument() {

        Long dataCount = (Long) movieRepository.count();
        Long pages = dataCount / 1000;

        for (int i = 0; i <= pages; i ++) {
            List<MovieDocument> movieDocumentList = movieRepository.findAll(PageRequest.of(i, 1000))
                    .stream().map(MovieDocument :: fromEntity).collect(Collectors.toList());

            movieSearchRepository.saveAll(movieDocumentList);
        }

        return dataCount;
    }

    @Override
    public List<MovieDocument> searchMovie(String movieName, String lang) {

        if (lang.equals("ko-KR")) {
            return movieSearchRepository.findAllByTitleKor(movieName);
        } else if (lang.equals("eng")) {
            return movieSearchRepository.findAllByTitleEng(movieName);
        }

        return movieSearchRepository.findAllByTitleEng(movieName);
    }
}
