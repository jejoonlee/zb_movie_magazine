package com.jejoonlee.movmag.app.movie.dto;

import com.jejoonlee.movmag.app.movie.domain.GenreEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class GenreDto {
    private Long genreId;
    private String genreEng;
    private String genreKor;

    public static GenreEntity toEntity(GenreDto genreDto) {
        return new GenreEntity(
                genreDto.getGenreId(),
                genreDto.getGenreEng(),
                genreDto.getGenreKor());
    }

    public static GenreDto fromEntity(GenreEntity genreEntity) {
        return GenreDto.builder()
                .genreId(genreEntity.getGenreId())
                .genreEng(genreEntity.getNameEng())
                .genreKor(genreEntity.getNameKor())
                .build();
    }
}
