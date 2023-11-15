package com.jejoonlee.movmag.app.movie.service.impl;

import com.jejoonlee.movmag.app.movie.domain.CastEntity;
import com.jejoonlee.movmag.app.movie.domain.GenreEntity;
import com.jejoonlee.movmag.app.movie.domain.MovieEntity;
import com.jejoonlee.movmag.app.movie.dto.*;
import com.jejoonlee.movmag.app.movie.repository.CastRepository;
import com.jejoonlee.movmag.app.movie.repository.GenreRepository;
import com.jejoonlee.movmag.app.movie.repository.MovieRepository;
import com.jejoonlee.movmag.app.movie.service.MovieExternalApiClient;
import com.jejoonlee.movmag.app.movie.service.MovieService;
import com.jejoonlee.movmag.exception.ErrorCode;
import com.jejoonlee.movmag.exception.MovieException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;
    private final GenreRepository genreRepository;
    private final CastRepository castRepository;

    private final MovieExternalApiClient movieExternalApiClient;

    private String LANG_KOREAN = "ko-KR";
    private String LANG_ENG = "en-US";

    private void saveGenre(MovieExternalApiDto.GenreList genreArrayEng, MovieExternalApiDto.GenreList genreArrayKor) {

        genreRepository.save(GenreDto.toEntity(GenreDto.builder()
                        .genreId(0L)
                        .genreKor("장르 없음")
                        .genreEng("No Genre")
                .build()));

        List<MovieExternalApiDto.Genre> genreEngList = genreArrayEng.getGenres();
        List<MovieExternalApiDto.Genre> genreKorList = genreArrayKor.getGenres();

        for (int i = 0; i < genreEngList.size(); i ++) {
            MovieExternalApiDto.Genre genreEng = genreEngList.get(i);

            Long genreEngId = genreEng.getId();
            String genreNameEng = genreEng.getName();

            GenreDto genreDto = GenreDto.builder()
                    .genreId(genreEngId)
                    .genreEng(genreNameEng)
                    .build();

            genreRepository.save(GenreDto.toEntity(genreDto));
        }

        for (int i = 0; i < genreKorList.size(); i++) {
            MovieExternalApiDto.Genre genreKor = genreKorList.get(i);

            Long genreKorId = genreKor.getId();
            String genreNameKor = genreKor.getName();

            GenreDto genreDto;

            if (!genreRepository.existsById(genreKorId)) {
                genreDto = GenreDto.builder()
                        .genreId(genreKorId)
                        .genreKor(genreNameKor)
                        .build();
            } else {
                GenreEntity genreEntity = genreRepository.findById(genreKorId)
                        .orElseThrow(() -> new MovieException(ErrorCode.INTERNAL_SERVER_ERROR));

                genreDto = GenreDto.fromEntity(genreEntity);
                genreDto.setGenreKor(genreNameKor);
            }

            genreRepository.save(GenreDto.toEntity(genreDto));
        }
    }

    // 가지고 온 영화 리스트 저장하기
    private int saveMovie(MovieExternalApiDto.MovieList movieListEng, MovieExternalApiDto.MovieList movieListKor) {

        int count = 0;

        List<MovieExternalApiDto.MovieInfo> movieInfoListEng = movieListEng.getResults();
        List<MovieExternalApiDto.MovieInfo> movieInfoListKor = movieListKor.getResults();

        for (int i = 0; i < movieInfoListEng.size(); i++) {
            MovieExternalApiDto.MovieInfo movie = movieInfoListEng.get(i);

            List<Long> genreIds = movie.getGenreIds();

            if (!movieRepository.existsById(movie.getId())) {

                ArrayList<Long> genreIdArray = new ArrayList<>();
                Map<String, Object> genreIdMap = new HashMap<>();

                if (genreIds.size() != 0) {
                    for (Long id : genreIds) {
                        genreIdArray.add(id);
                    }
                } else {
                    genreIdArray.add(0L);
                }

                genreIdMap.put("장르ID", genreIdArray);

                MovieDto movieDto = MovieDto.builder()
                        .movieId(movie.getId())
                        .genreId(genreIdMap)
                        .titleEng(movie.getTitle())
                        .overviewEng(movie.getOverview())
                        .releasedDate(movie.getReleaseDate())
                        .movieScore(0.0)
                        .posterPath(movie.getPosterPath())
                        .build();

                movieRepository.save(MovieDto.toEntity(movieDto));
                count++;
            }
        }

        for (int i = 0; i < movieInfoListKor.size(); i++) {
            MovieExternalApiDto.MovieInfo movie = movieInfoListKor.get(i);
            Long movieId = movie.getId();

            List<Long> genreIds = movie.getGenreIds();

            if (!movieRepository.existsById(movieId)) {

                ArrayList<Long> genreIdArray = new ArrayList<>();
                Map<String, Object> genreIdMap = new HashMap<>();

                if (genreIds.size() != 0) {
                    for (Long id : genreIds) {
                        genreIdArray.add(id);
                    }
                } else {
                    genreIdArray.add(0L);
                }

                genreIdMap.put("장르ID", genreIdArray);

                MovieDto movieDto = MovieDto.builder()
                        .movieId(movie.getId())
                        .genreId(genreIdMap)
                        .titleKor(movie.getTitle())
                        .overviewKor(movie.getOverview())
                        .releasedDate(movie.getReleaseDate())
                        .movieScore(0.0)
                        .posterPath(movie.getPosterPath())
                        .build();

                movieRepository.save(MovieDto.toEntity(movieDto));

                count ++;
            } else {
                MovieEntity movieEntity = movieRepository.findById(movieId)
                        .orElseThrow(() -> new MovieException(ErrorCode.INTERNAL_SERVER_ERROR));

                MovieDto movieDto = MovieDto.fromEntity(movieEntity);

                movieDto.setTitleKor(movie.getTitle());
                movieDto.setOverviewKor(movie.getOverview());

                movieRepository.save(MovieDto.toEntity(movieDto));
            }
        }

        return count;
    }

    private void saveRuntimeToMovie(Long movieId, Long runtime) {

        MovieEntity movieEntity = movieRepository.findById(movieId)
                .orElseThrow(() -> new MovieException(ErrorCode.MOVIE_NOT_FOUND));

        MovieDto movieDto = MovieDto.fromEntity(movieEntity);

        movieDto.setRuntime(runtime);

        movieRepository.save(MovieDto.toEntity(movieDto));
    }

    private int saveCast(List<MovieExternalApiDto.CastInfo> castList, Long movieId) {
        int count = 0;

        for (MovieExternalApiDto.CastInfo castInfo : castList) {

            String role = castInfo.getKnownForDepartment();

            Long castId = castInfo.getId();

            CastDto castDto;

            if (!castRepository.existsById(castId)) {
                JSONArray movieArray = new JSONArray();

                movieArray.add(movieId);

                JSONObject object = new JSONObject();
                object.put("movieId", movieArray);

                castDto = CastDto.builder()
                        .castId(castId)
                        .MovieId(object)
                        .nameEng(castInfo.getName())
                        .role(role)
                        .build();

                count++;

            } else {

                // 이미 캐스트가 저장되어 있다는 것은, 다른 영화를 통해 저장이 되었다는 의미
                // 영화만 추가해주면 된다
                CastEntity castEntity = castRepository.findById(castId)
                        .orElseThrow(() -> new MovieException(ErrorCode.CAST_NOT_FOUND));

                castDto = CastDto.fromEntity(castEntity);

                Map<String, Object> movieIdMap = castDto.getMovieId();

                ArrayList<Integer> movieIdList = (ArrayList<Integer>) movieIdMap.get("movieId");

                movieIdList.add(movieId.intValue());

                List<Long> movieIdResult = movieIdList.stream()
                        .map(id -> Long.valueOf(id))
                        .distinct()
                        .collect(Collectors.toList());

                movieIdMap.put("movieId", movieIdResult);

                castDto.setMovieId(movieIdMap);
            }

            castRepository.save(CastDto.toEntity(castDto));
        }

        return count;
    }

    private int[] saveMovies(String apiKey, int start, int movieNum) {

        int[] counts = new int[2];

        // ===== 영화 정보 페이지, 하나씩 가지고 오기 (for문으로 1부터 500) =====

        for (int pageNum = start; pageNum < start + movieNum; pageNum++) {
            String pg = String.valueOf(pageNum);

            log.info("Get MovieList page {} start : {}", pageNum, LocalDateTime.now());
            MovieExternalApiDto.MovieList movieListEng = movieExternalApiClient.getMovieList(apiKey, LANG_ENG, pg);
            MovieExternalApiDto.MovieList movieListKor = movieExternalApiClient.getMovieList(apiKey, LANG_KOREAN, pg);
            log.info("Get MovieList page {} finish : {}", pageNum, LocalDateTime.now());

            log.info("Save MovieList page {} start : {}", pageNum, LocalDateTime.now());
            counts[0] += saveMovie(movieListEng, movieListKor);
            log.info("Save MovieList page {} finish : {}", pageNum, LocalDateTime.now());

            List<MovieExternalApiDto.MovieInfo> movieInfo = movieListEng.getResults();

            // 디테일에서 런타임 저장 + cast 저장
            for (int i = 0; i < movieInfo.size(); i ++) {
//
                MovieExternalApiDto.MovieInfo movie = movieInfo.get(i);
                Long movieId = movie.getId();

                MovieExternalApiDto.MovieDetail movieDetail =
                        movieExternalApiClient.getMovieDetail(apiKey, movieId);

//                // 영화 런타임 저장
                saveRuntimeToMovie(movieId, movieDetail.getRuntime());
//
                log.info("Save Cast for movie {} start : {}", movieId, LocalDateTime.now());
//
//                // cast 저장
                List<MovieExternalApiDto.CastInfo> cast = movieDetail.getCredits().getCast();
//
                counts[1] += saveCast(cast, movieId);
//
                log.info("Save Cast for movie {} finish : {}", movieId, LocalDateTime.now());
//
            }
        }

        return counts;
    }

    @Async
    @Override
    public UpdateMovie.Response saveAllMovies(UpdateMovie.Request request) {

        String apiKey = "Bearer " + request.getApiKey();

        // 장르 저장하기
        log.info("Get Genre start : {}", LocalDateTime.now());
        MovieExternalApiDto.GenreList genreListEng = movieExternalApiClient.getGenre(apiKey, LANG_ENG);
        MovieExternalApiDto.GenreList genreListKor = movieExternalApiClient.getGenre(apiKey, LANG_KOREAN);
        log.info("Get Genre finish : {}", LocalDateTime.now());

        log.info("Save Genre start : {}", LocalDateTime.now());
        saveGenre(genreListEng, genreListKor);
        log.info("Save Genre finish : {}", LocalDateTime.now());

        int[] counts = saveMovies(apiKey, 1, 500);

        return UpdateMovie.Response.builder()
                .message("DB에 저장된 영화와 캐스트. 이미 저장이 되어 있는 데이터일 수도 있습니다 (0이 나올시).")
                .movieNum(counts[0])
                .peopleNum(counts[1])
                .build();
    }

    // 오늘 기준 2달 전까지 영화관에서 상영했던, 또는 하고 있는 모든 영화를 DB에 저장하기
    @Async
    @Override
    public UpdateMovie.Response updateNewMovies(UpdateMovie.Request request) {

        String apiKey = "Bearer " + request.getApiKey();

        // 장르 저장하기
        log.info("Get New Genre start : {}", LocalDateTime.now());
        MovieExternalApiDto.GenreList genreListEng = movieExternalApiClient.getGenre(apiKey, LANG_ENG);
        MovieExternalApiDto.GenreList genreListKor = movieExternalApiClient.getGenre(apiKey, LANG_KOREAN);
        log.info("Get New Genre finish : {}", LocalDateTime.now());

        log.info("Save New Genre start : {}", LocalDateTime.now());
        saveGenre(genreListEng, genreListKor);
        log.info("Save New Genre finish : {}", LocalDateTime.now());

        int pages = movieExternalApiClient.getMovieList(apiKey, LANG_ENG, "1").getTotalPages();

        int[] counts = saveMovies(apiKey, 1, pages);

        return UpdateMovie.Response.builder()
                .message("DB에 저장된 영화와 캐스트. 이미 저장이 되어 있는 데이터일 수도 있습니다 (0이 나올시).")
                .movieNum(counts[0])
                .peopleNum(counts[1])
                .build();
    }
}
