package com.jejoonlee.movmag.app.elasticsearch.document;

import com.jejoonlee.movmag.app.movie.domain.CastEntity;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.util.HashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Setting(settingPath = "static/elastic/elastic-settings.json")
@Mapping(mappingPath = "static/elastic/cast-mapping.json")
@Document(indexName = "cast_index") // 정해줄 index (table) 이름
public class CastDocument {

    @Id
    @Field(name="cast_id", type = FieldType.Keyword)
    private Long castId;

    @Field(name="movie_id", type = FieldType.Object)
    private Map<String, Object> movieId = new HashMap<>();

    @Field(name="name_eng", type = FieldType.Text)
    private String nameEng;

    @Field(name="role", type = FieldType.Text)
    private String role;

    public static CastDocument fromEntity(CastEntity castEntity) {
        return CastDocument.builder()
                .castId(castEntity.getCastId())
                .movieId(castEntity.getMovieId())
                .nameEng(castEntity.getNameEng())
                .role(castEntity.getRole())
                .build();
    }
}
