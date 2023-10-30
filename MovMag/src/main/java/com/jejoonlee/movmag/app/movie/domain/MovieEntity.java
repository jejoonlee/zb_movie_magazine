package com.jejoonlee.movmag.app.movie.domain;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.HashSet;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Entity(name="movie")
public class MovieEntity {

    @Id
    @Column(name = "movie_id")
    private Long movieId;

    @Column(name = "genre")
    @Convert(converter = LongSetConverter.class)
    private HashSet<Long> genreId = new HashSet<>();

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
