package com.jejoonlee.movmag.app.movie.service.impl;

import com.jejoonlee.movmag.app.movie.domain.GenreEntity;
import com.jejoonlee.movmag.app.movie.dto.GenreDto;
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
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;

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


    private static ErrorCode isSuccessful(JSONObject result) {

        if (result.containsKey("status_code")) {
            ErrorCode errorCode = TmdbError.check((Long) result.get("status_code"));
            return errorCode;
        } else {
            return ErrorCode.TMDB_SUCCESS;
        }

    }

    @Override
    public UpdateMovie.Response saveAllMovies(UpdateMovie.Request request) throws ParseException {

        // 장르 저장하기
        log.info("Get Genre start : {}", LocalDateTime.now());
        JSONArray genreArrayEng = getGenre(request, LANG_ENG);
        JSONArray genreArrayKor = getGenre(request, LANG_KOREAN);
        log.info("Get Genre finish : {}", LocalDateTime.now());

        log.info("Save Genre start : {}", LocalDateTime.now());
        saveGenre(genreArrayEng, genreArrayKor);
        log.info("Save Genre finish : {}", LocalDateTime.now());



        // ===== 영화 정보 페이지, 하나씩 가지고 오기 (for문으로 1부터 500) =====
            // 페이지 안에 있는 영화들 순회하며 해당 Detail 페이지의 credits에서 cast 정보 가지고 오기 (그 중에 배우와 감독만)
            // Cast_id가 db에 있으면, 영화 ID만 리스트 안에 추가로 저장
            // 없으면 그대로 저장

        return UpdateMovie.Response.builder()
                .movieNum(1)
                .peopleNum(1)
                .build();
    }
}
