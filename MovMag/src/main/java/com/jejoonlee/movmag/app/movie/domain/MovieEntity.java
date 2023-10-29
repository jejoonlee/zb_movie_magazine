package com.jejoonlee.movmag.app.movie.domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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
    @Convert(converter = LongArrayConverter.class)
    private List<Long> genreId = new ArrayList<>();

    @Column(name = "title_eng")
    private String titleEng;

    @Column(name = "title_kor")
    private String titleKor;

    @Column(name = "overview_eng")
    private String overviewEng;

    @Column(name = "overview_kor")
    private String overviewKor;

    @Column(name = "released_date")
    private String releasedDate;

    @Column(name = "movie_score")
    private Double movieScore;

    @Column(name = "runtime")
    private int runtime;

    @Column(name = "poster_path")
    private String posterPath;
}
