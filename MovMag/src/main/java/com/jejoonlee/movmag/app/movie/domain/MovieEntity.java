package com.jejoonlee.movmag.app.movie.domain;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Entity(name="movie")
@TypeDef(name = "json", typeClass = JsonType.class)
public class MovieEntity {

    @Id
    @Column(name = "movie_id")
    private Long movieId;

    @Type(type="json")
    @Column(name="genre_id", columnDefinition = "json")
    private Map<String, Object> genreId = new HashMap<>();

    @Column(name = "title_eng")
    private String titleEng;

    @Column(name = "title_kor")
    private String titleKor;

    @Column(name = "overview_eng", length = 1000)
    private String overviewEng;

    @Column(name = "overview_kor", length = 1000)
    private String overviewKor;

    @Column(name = "released_date", length = 20)
    private String releasedDate;

    @Column(name = "movie_score")
    private Double movieScore;

    @Column(name = "runtime")
    private Long runtime;

    @Column(name = "poster_path")
    private String posterPath;
}
