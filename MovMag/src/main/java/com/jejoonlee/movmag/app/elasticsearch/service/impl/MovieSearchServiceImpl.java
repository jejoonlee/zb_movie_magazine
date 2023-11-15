package com.jejoonlee.movmag.app.elasticsearch.service.impl;

import com.jejoonlee.movmag.app.elasticsearch.document.MovieDocument;
import com.jejoonlee.movmag.app.elasticsearch.dto.MovieElsDto;
import com.jejoonlee.movmag.app.elasticsearch.repository.MovieSearchRepository;
import com.jejoonlee.movmag.app.elasticsearch.service.MovieSearchService;
import com.jejoonlee.movmag.app.movie.dto.GenreDto;
import com.jejoonlee.movmag.app.movie.repository.GenreRepository;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MovieSearchServiceImpl implements MovieSearchService {

    private final MovieRepository movieRepository;
    private final MovieSearchRepository movieSearchRepository;
    private final GenreRepository genreRepository;
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

    private MovieElsDto.PageInfo getMovieList(String movieName, String lang, int page){

        Page<MovieDocument> movieDocumentPage;
        
        List<MovieElsDto.Response> result = new ArrayList<>();

        // 입력한 페이지에 대한 영화 데이터 가지고 오기
        if (lang.equals("korean")) {
            movieDocumentPage =  movieSearchRepository
                    .findAllByTitleKorOrderByReleasedDateDesc(movieName, PageRequest.of(page - 1, 10));
        } else if (lang.equals("english")) {
            movieDocumentPage = movieSearchRepository
                    .findAllByTitleEngOrderByReleasedDateDesc(movieName, PageRequest.of(page - 1, 10));
        } else {
            throw new MovieException(ErrorCode.WRONG_LANGUAGE);
        }

        if (page <= 0 && page > movieDocumentPage.getTotalPages())
            throw new MovieException(ErrorCode.PAGE_NOT_FOUND);

        List<MovieDocument> movieDocumentList = movieDocumentPage.getContent();

        if (movieDocumentList.size() == 0)
            throw new MovieException(ErrorCode.NO_MOVIE_FOUND);

        Long count = (Long.valueOf(page) - 1L) * 10 + 1L;

        // 검색한 영화 데이터 저장하기 (장르는 ID말고 이름으로)
        for (int i = 0; i < movieDocumentList.size(); i++) {

            MovieDocument movie = movieDocumentList.get(i);
            MovieElsDto.Response movieElsDto = MovieElsDto.Response.fromDocument(movie);

            List<Integer> genres = (ArrayList<Integer>) movie.getGenreId().get("장르ID");

            List<String> genre = new ArrayList<>();

            for (int num : genres) {
                Long genreId = Long.valueOf(num);

                GenreDto genreDto = GenreDto.fromEntity(genreRepository.getOne(genreId));

                if (lang.equals("korean")) {
                    genre.add(genreDto.getGenreKor());
                } else if (lang.equals("english")) {
                    genre.add(genreDto.getGenreEng());
                }
            }

            movieElsDto.setNumber(count ++);
            movieElsDto.setGenre(genre);

            result.add(movieElsDto);
        }

        return MovieElsDto.PageInfo.builder()
                .page(page)
                .totalPage(movieDocumentPage.getTotalPages())
                .foundDataNum(movieDocumentPage.getTotalElements())
                .data(result)
                .build();
    }

    @Override
    public MovieElsDto.PageInfo searchMovie(String movieName, String lang, int page) {
        return getMovieList(movieName, lang, page);
    }
}
