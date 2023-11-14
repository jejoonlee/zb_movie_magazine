package com.jejoonlee.movmag.app.elasticsearch.document;

import com.jejoonlee.movmag.app.movie.domain.MovieEntity;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Setting(settingPath = "static/elastic/elastic-settings.json")
@Mapping(mappingPath = "static/elastic/movie-mapping.json")
@Document(indexName = "movie_index") // 정해줄 index (table) 이름
public class MovieDocument {

    @Id
    @Field(name="movie_id", type = FieldType.Keyword)
    private Long movieId;

    @Field(name="genre_id", type = FieldType.Object)
    private Map<String, Object> genreId = new HashMap<>();

    @Field(name="title_eng", type = FieldType.Text)
    private String titleEng;

    @Field(name="title_kor", type = FieldType.Text)
    private String titleKor;

    @Field(name="overview_eng", type = FieldType.Text)
    private String overviewEng;

    @Field(name="overview_kor", type = FieldType.Text)
    private String overviewKor;

    @Field(name="released_date", type = FieldType.Date)
    private LocalDate releasedDate;

    @Field(name="movie_score", type = FieldType.Double)
    private Double movieScore;

    @Field(name="runtime", type = FieldType.Long)
    private Long runtime;

    @Field(name="poster_path", type = FieldType.Text)
    private String posterPath;

    public static MovieDocument fromEntity(MovieEntity movieEntity) {

        // 영화 개봉 날짜가 없을 때 사용 (영화 역사상 첫 영화 개봉 날짜)
        String firstEverMovieReleased = "1895-12-28";

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate releasedDate;

        if (!movieEntity.getReleasedDate().equals("")) {
            releasedDate = LocalDate.parse(movieEntity.getReleasedDate(), dtf);
        } else {
            releasedDate = LocalDate.parse(firstEverMovieReleased, dtf);
        }

        return MovieDocument.builder()
                .movieId(movieEntity.getMovieId())
                .genreId(movieEntity.getGenreId())
                .titleEng(movieEntity.getTitleEng())
                .titleKor(movieEntity.getTitleKor())
                .overviewEng(movieEntity.getOverviewEng())
                .overviewKor(movieEntity.getOverviewKor())
                .releasedDate(releasedDate)
                .movieScore(movieEntity.getMovieScore())
                .runtime(movieEntity.getRuntime())
                .posterPath(movieEntity.getPosterPath())
                .build();
    }

}
