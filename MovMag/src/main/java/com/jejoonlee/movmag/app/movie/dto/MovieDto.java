package com.jejoonlee.movmag.app.movie.dto;

import com.jejoonlee.movmag.app.movie.domain.MovieEntity;
import lombok.*;

import java.util.HashSet;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovieDto {

    private Long movieId;
    private HashSet<Long> genreId = new HashSet<>();
    private String titleEng;
    private String titleKor;
    private String overviewEng;
    private String overviewKor;
    private String releasedDate;
    private Double movieScore;
    private Long runtime;
    private String posterPath;

    public static MovieEntity toEntity(MovieDto movieDto) {
        return new MovieEntity(
                movieDto.getMovieId(),
                movieDto.getGenreId(),
                movieDto.getTitleEng(),
                movieDto.getTitleKor(),
                movieDto.getOverviewEng(),
                movieDto.getOverviewKor(),
                movieDto.getReleasedDate(),
                movieDto.getMovieScore(),
                movieDto.getRuntime(),
                movieDto.getPosterPath()
        );
    }

    public static MovieDto fromEntity(MovieEntity movieEntity) {
        return MovieDto.builder()
                .movieId(movieEntity.getMovieId())
                .genreId(movieEntity.getGenreId())
                .titleEng(movieEntity.getTitleEng())
                .titleKor(movieEntity.getTitleKor())
                .overviewEng(movieEntity.getOverviewEng())
                .overviewKor(movieEntity.getOverviewKor())
                .releasedDate(movieEntity.getReleasedDate())
                .movieScore(movieEntity.getMovieScore())
                .runtime(movieEntity.getRuntime())
                .posterPath(movieEntity.getPosterPath())
                .build();
    }
}
