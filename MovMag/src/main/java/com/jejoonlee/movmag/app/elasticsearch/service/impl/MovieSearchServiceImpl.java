package com.jejoonlee.movmag.app.elasticsearch.service.impl;

import com.jejoonlee.movmag.app.elasticsearch.document.MovieDocument;
import com.jejoonlee.movmag.app.elasticsearch.dto.MovieElsDto;
import com.jejoonlee.movmag.app.elasticsearch.repository.MovieSearchRepository;
import com.jejoonlee.movmag.app.elasticsearch.service.MovieSearchService;
import com.jejoonlee.movmag.app.movie.domain.GenreEntity;
import com.jejoonlee.movmag.app.movie.dto.GenreDto;
import com.jejoonlee.movmag.app.movie.repository.GenreRepository;
import com.jejoonlee.movmag.app.movie.repository.MovieRepository;
import com.jejoonlee.movmag.exception.ErrorCode;
import com.jejoonlee.movmag.exception.MovieException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    private List<MovieElsDto.Response> getMovieList(String movieName, String lang){

        List<MovieDocument> movieDocumentList;
        List<MovieElsDto.Response> result = new ArrayList<>();

        if (lang.equals("korean")) {
            movieDocumentList =  movieSearchRepository.findAllByTitleKor(movieName);
        } else if (lang.equals("english")) {
            movieDocumentList = movieSearchRepository.findAllByTitleEng(movieName);
        } else {
            throw new MovieException(ErrorCode.WRONG_LANGUAGE);
        }

        if (movieDocumentList.size() == 0)
            throw new MovieException(ErrorCode.NO_MOVIE_FOUND);

        for (int i = 0; i < movieDocumentList.size(); i++) {

            MovieDocument movie = movieDocumentList.get(i);
            MovieElsDto.Response movieElsDto = MovieElsDto.Response.fromDocument(movie);

            List<Integer> genres = (ArrayList<Integer>) movie.getGenreId().get("장르ID");

            List<String> genre = new ArrayList<>();

            for (int num : genres) {
                Long genreId = Long.valueOf(num);

                GenreEntity genreEntity = genreRepository.findById(genreId).get();
                GenreDto genreDto = GenreDto.fromEntity(genreEntity);

                if (lang.equals("korean")) {
                    genre.add(genreDto.getGenreKor());
                } else if (lang.equals("english")) {
                    genre.add(genreDto.getGenreEng());
                }
            }

            movieElsDto.setGenre(genre);

            result.add(movieElsDto);
        }

        return result;
    }

    @Override
    public List<MovieElsDto.Response> searchMovie(String movieName, String lang) {

        return getMovieList(movieName, lang);
    }
}
