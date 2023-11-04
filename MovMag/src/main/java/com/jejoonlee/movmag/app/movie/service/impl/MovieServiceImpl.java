package com.jejoonlee.movmag.app.movie.service.impl;

import com.jejoonlee.movmag.app.movie.domain.CastEntity;
import com.jejoonlee.movmag.app.movie.domain.GenreEntity;
import com.jejoonlee.movmag.app.movie.domain.MovieEntity;
import com.jejoonlee.movmag.app.movie.dto.CastDto;
import com.jejoonlee.movmag.app.movie.dto.GenreDto;
import com.jejoonlee.movmag.app.movie.dto.MovieDto;
import com.jejoonlee.movmag.app.movie.dto.UpdateMovie;
import com.jejoonlee.movmag.app.movie.repository.CastRepository;
import com.jejoonlee.movmag.app.movie.repository.GenreRepository;
import com.jejoonlee.movmag.app.movie.repository.MovieRepository;
import com.jejoonlee.movmag.app.movie.service.MovieService;
import com.jejoonlee.movmag.exception.ErrorCode;
import com.jejoonlee.movmag.exception.MovieException;
import com.jejoonlee.movmag.exception.TmdbError;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;
    private final GenreRepository genreRepository;
    private final CastRepository castRepository;

    private String LANG_KOREAN = "ko-KR";
    private String LANG_ENG = "en-US";

    private static JSONObject tmdbGetResult(String urlString, String apiKey) throws ParseException {

        StringBuilder urlBuilder = new StringBuilder(urlString);
        StringBuilder sb = new StringBuilder();

        try {
            URL url = new URL(urlBuilder.toString());

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-type", "application/json");
            connection.setRequestProperty("Authorization", "Bearer " + apiKey);


            BufferedReader br;

            if (connection.getResponseCode() >= 200 && connection.getResponseCode() <= 300) {
                br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            }

            String line;

            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();
            connection.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }

        JSONObject result = (JSONObject) new JSONParser().parse(sb.toString());

        return result;
    }

    private JSONArray getGenre(UpdateMovie.Request request, String language) throws ParseException {
        // 장르 업데이트 하기
        String genreURL = "https://api.themoviedb.org/3/genre/movie/list?language=";

        JSONObject genreResult = tmdbGetResult(genreURL + language,
                request.getApiKey());

        ErrorCode genreEngErrorCode = isSuccessful(genreResult);

        if (!genreEngErrorCode.equals(ErrorCode.TMDB_SUCCESS)) {
            throw new MovieException(genreEngErrorCode);
        }

        JSONArray genreList = (JSONArray) genreResult.get("genres");

        return genreList;
    }

    private void saveGenre(JSONArray genreArrayEng, JSONArray genreArrayKor) {
        genreRepository.save(GenreDto.toEntity(GenreDto.builder()
                        .genreId(0L)
                        .genreKor("장르 없음")
                        .genreEng("No Genre")
                .build()));

        for (int i = 0; i < genreArrayEng.size(); i ++) {
            JSONObject genreEng = (JSONObject) genreArrayEng.get(i);

            Long genreEngId = (Long) genreEng.get("id");
            String genreNameEng = (String) genreEng.get("name");

            GenreDto genreDto = GenreDto.builder()
                    .genreId(genreEngId)
                    .genreEng(genreNameEng)
                    .build();

            genreRepository.save(GenreDto.toEntity(genreDto));
        }

        for (int i = 0; i < genreArrayKor.size(); i++) {
            JSONObject genreKor = (JSONObject) genreArrayKor.get(i);

            Long genreKorId = (Long) genreKor.get("id");
            String genreNameKor = (String) genreKor.get("name");

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

    // 영화 리스트 가지고 오기
    private JSONArray getMovieList(UpdateMovie.Request request, String url) throws ParseException {

        JSONObject movieResult = tmdbGetResult(url,
                request.getApiKey());

        ErrorCode genreEngErrorCode = isSuccessful(movieResult);

        if (!genreEngErrorCode.equals(ErrorCode.TMDB_SUCCESS)) {
            throw new MovieException(genreEngErrorCode);
        }

        JSONArray movieList = (JSONArray) movieResult.get("results");

        return movieList;
    }

    // 가지고 온 영화 리스트 저장하기
    private int saveMovie(JSONArray movieListEng, JSONArray movieListKor) {

        int count = 0;

        for (int i = 0; i < movieListEng.size(); i++) {
            JSONObject movie = (JSONObject) movieListEng.get(i);
            HashSet<Long> set = new HashSet<>();

            JSONArray genreIds = (JSONArray) movie.get("genre_ids");

            if (!movieRepository.existsById((Long) movie.get("id"))) {

                ArrayList<Long> genreIdArray = new ArrayList<>();
                Map<String, Object> genreIdMap = new HashMap<>();

                if (genreIds.size() != 0) {
                    for (Object id : genreIds) {
                        genreIdArray.add((Long) id);
                    }
                } else {
                    genreIdArray.add(0L);
                }

                genreIdMap.put("장르ID", genreIdArray);

                MovieDto movieDto = MovieDto.builder()
                        .movieId((Long) movie.get("id"))
                        .genreId(genreIdMap)
                        .titleEng((String) movie.get("title"))
                        .overviewEng((String) movie.get("overview"))
                        .releasedDate((String) movie.get("release_date"))
                        .movieScore(0.0)
                        .posterPath((String) movie.get("poster_path"))
                        .build();

                movieRepository.save(MovieDto.toEntity(movieDto));
                count++;
            }
        }

        for (int i = 0; i < movieListKor.size(); i++) {
            JSONObject movie = (JSONObject) movieListKor.get(i);
            Long movieId = (Long) movie.get("id");

            JSONArray genreIds = (JSONArray) movie.get("genre_ids");

            if (!movieRepository.existsById(movieId)) {

                ArrayList<Long> genreIdArray = new ArrayList<>();
                Map<String, Object> genreIdMap = new HashMap<>();

                if (genreIds.size() != 0) {
                    for (Object id : genreIds) {
                        genreIdArray.add((Long) id);
                    }
                } else {
                    genreIdArray.add(0L);
                }

                genreIdMap.put("장르ID", genreIdArray);

                MovieDto movieDto = MovieDto.builder()
                        .movieId((Long) movie.get("id"))
                        .genreId(genreIdMap)
                        .titleKor((String) movie.get("title"))
                        .overviewKor((String) movie.get("overview"))
                        .releasedDate((String) movie.get("release_date"))
                        .movieScore(0.0)
                        .posterPath((String) movie.get("poster_path"))
                        .build();

                movieRepository.save(MovieDto.toEntity(movieDto));

                count ++;
            } else {
                MovieEntity movieEntity = movieRepository.findById(movieId)
                        .orElseThrow(() -> new MovieException(ErrorCode.INTERNAL_SERVER_ERROR));

                MovieDto movieDto = MovieDto.fromEntity(movieEntity);

                movieDto.setTitleKor((String) movie.get("title"));
                movieDto.setOverviewKor((String) movie.get("overview"));

                movieRepository.save(MovieDto.toEntity(movieDto));
            }
        }

        return count;
    }

    // 영화 디테일에서 감독 또는 배우 정보 가지고 오기
    private JSONObject getMovieDetail(UpdateMovie.Request request, Long movieId) throws ParseException {

        String movieUrl = String.format(
                "https://api.themoviedb.org/3/movie/%d?append_to_response=credits&language=en-US",
                movieId);

        JSONObject movieDetailResult = tmdbGetResult(movieUrl,
                request.getApiKey());

        ErrorCode genreEngErrorCode = isSuccessful(movieDetailResult);

        if (!genreEngErrorCode.equals(ErrorCode.TMDB_SUCCESS)) {
            throw new MovieException(genreEngErrorCode);
        }

        return movieDetailResult;
    }

    private void saveRuntimeToMovie(JSONObject movieDetail) {
        Long runtime = (Long) movieDetail.get("runtime");

        MovieEntity movieEntity = movieRepository.findById((Long) movieDetail.get("id"))
                .orElseThrow(() -> new MovieException(ErrorCode.MOVIE_NOT_FOUND));

        MovieDto movieDto = MovieDto.fromEntity(movieEntity);

        movieDto.setRuntime(runtime);

        movieRepository.save(MovieDto.toEntity(movieDto));
    }

    private int saveCast(JSONArray castList, Long movieId) {
        int count = 0;

        for (Object fromCastList : castList) {

            JSONObject cast = (JSONObject) fromCastList;

            String role = (String) cast.get("known_for_department");

            Long castId = (Long) cast.get("id");

            boolean savedCast = castRepository.existsById(castId);

            CastDto castDto;

            if (!savedCast) {
                JSONArray movieArray = new JSONArray();

                movieArray.add(movieId);

                JSONObject object = new JSONObject();
                object.put("movieId", movieArray);

                castDto = CastDto.builder()
                        .castId(castId)
                        .MovieId(object)
                        .nameEng((String) cast.get("name"))
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

                Set<Long> movieIdSet = new HashSet<>();

                ArrayList<Integer> movieIdList = (ArrayList<Integer>) movieIdMap.get("movieId");

                for (int id : movieIdList) movieIdSet.add((long) id);

                movieIdSet.add(movieId);

                movieIdMap.put("movieId", new ArrayList<>(movieIdSet));

                castDto.setMovieId(movieIdMap);

            }

            castRepository.save(CastDto.toEntity(castDto));
        }

        return count;
    }

    private static ErrorCode isSuccessful(JSONObject result) {

        if (result.containsKey("status_code")) {
            ErrorCode errorCode = TmdbError.check((Long) result.get("status_code"));
            return errorCode;
        } else {
            return ErrorCode.TMDB_SUCCESS;
        }

    }
    private int[] saveMovies(String movieUrl, int start, int movieNum, UpdateMovie.Request request) throws ParseException, InterruptedException {

        int[] counts = new int[2];

        // ===== 영화 정보 페이지, 하나씩 가지고 오기 (for문으로 1부터 500) =====

        for (int pageNum = start; pageNum < start + movieNum; pageNum++) {
            String pg = String.format("&page=%d", pageNum);

            String moviePopularUrlEng = movieUrl + LANG_ENG + pg;

            String moviePopularUrlKor = movieUrl + LANG_KOREAN + pg;

            log.info("Get MovieList page {} start : {}", pageNum, LocalDateTime.now());
            JSONArray movieListEng = getMovieList(request, moviePopularUrlEng);
            JSONArray movieListKor = getMovieList(request, moviePopularUrlKor);
            log.info("Get MovieList page {} finish : {}", pageNum, LocalDateTime.now());

            log.info("Save MovieList page {} start : {}", pageNum, LocalDateTime.now());
            counts[0] += saveMovie(movieListEng, movieListKor);
            log.info("Save MovieList page {} finish : {}", pageNum, LocalDateTime.now());


            // 디테일에서 런타임 저장 + cast 저장
            for (int i = 0; i < movieListEng.size(); i ++) {

                JSONObject movie = (JSONObject) movieListEng.get(i);
                Long movieIdForCast = (Long) movie.get("id");

                JSONObject movieDetail = getMovieDetail(request, (Long) movieIdForCast);

                // 영화 런타임 저장
                saveRuntimeToMovie(movieDetail);

                log.info("Save Cast for movie {} start : {}", movieIdForCast, LocalDateTime.now());

                // cast 저장
                JSONObject credits = (JSONObject) movieDetail.get("credits");
                JSONArray cast = (JSONArray) credits.get("cast");

                counts[1] += saveCast(cast, movieIdForCast);

                log.info("Save Cast for movie {} finish : {}", movieIdForCast, LocalDateTime.now());

            }
        }

        return counts;
    }

    private Long totalPages(UpdateMovie.Request request, String url) throws ParseException {

        JSONObject movieResult = tmdbGetResult(url,
                request.getApiKey());

        return (Long) movieResult.get("total_pages");
    }

    @Async
    @Override
    public UpdateMovie.Response saveAllMovies(UpdateMovie.Request request) throws ParseException, InterruptedException {

        // 장르 저장하기
        log.info("Get Genre start : {}", LocalDateTime.now());
        JSONArray genreArrayEng = getGenre(request, LANG_ENG);
        JSONArray genreArrayKor = getGenre(request, LANG_KOREAN);
        log.info("Get Genre finish : {}", LocalDateTime.now());

        log.info("Save Genre start : {}", LocalDateTime.now());
        saveGenre(genreArrayEng, genreArrayKor);
        log.info("Save Genre finish : {}", LocalDateTime.now());

        String moviePopularUrl = "https://api.themoviedb.org/3/movie/popular?language=";

        int[] counts = saveMovies(moviePopularUrl, 1, 500, request);

        return UpdateMovie.Response.builder()
                .message("DB에 저장된 영화와 캐스트. 이미 저장이 되어 있는 데이터일 수도 있습니다 (0이 나올시).")
                .movieNum(counts[0])
                .peopleNum(counts[1])
                .build();
    }

    // 오늘 기준 2달 전까지 영화관에서 상영했던, 또는 하고 있는 모든 영화를 DB에 저장하기
    @Async
    @Override
    public UpdateMovie.Response updateNewMovies(UpdateMovie.Request request) throws ParseException, InterruptedException {

        // 장르 저장하기
        log.info("Get New Genre start : {}", LocalDateTime.now());
        JSONArray genreArrayEng = getGenre(request, LANG_ENG);
        JSONArray genreArrayKor = getGenre(request, LANG_KOREAN);
        log.info("Get New Genre finish : {}", LocalDateTime.now());

        log.info("Save New Genre start : {}", LocalDateTime.now());
        saveGenre(genreArrayEng, genreArrayKor);
        log.info("Save New Genre finish : {}", LocalDateTime.now());

        String movieNowPlayingUrl = "https://api.themoviedb.org/3/movie/now_playing?language=";

        int pages = Math.toIntExact(totalPages(request, movieNowPlayingUrl + LANG_ENG + "&page=1"));

        int[] counts = saveMovies(movieNowPlayingUrl, 1, pages, request);

        return UpdateMovie.Response.builder()
                .message("DB에 저장된 영화와 캐스트. 이미 저장이 되어 있는 데이터일 수도 있습니다 (0이 나올시).")
                .movieNum(counts[0])
                .peopleNum(counts[1])
                .build();
    }
}
