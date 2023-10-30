package com.jejoonlee.movmag.app.movie.dto;

import com.jejoonlee.movmag.app.movie.domain.CastEntity;
import lombok.*;

import java.util.HashSet;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CastDto {
    private Long castId;
    private HashSet<Long> MovieIds;
    private String nameEng;
    private String role;

    public static CastEntity toEntity(CastDto castDto) {
        return new CastEntity(
                castDto.getCastId(),
                castDto.getMovieIds(),
                castDto.getNameEng(),
                castDto.getRole());
    }

    public static CastDto fromEntity(CastEntity castEntity) {
        return CastDto.builder()
                .castId(castEntity.getCastId())
                .MovieIds(castEntity.getMovie())
                .nameEng(castEntity.getNameEng())
                .role(castEntity.getRole())
                .build();
    }

}
