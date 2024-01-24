package com.jejoonlee.movmag.app.movie.domain;

import com.jejoonlee.movmag.app.movie.dto.MovieExternalApiDto;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.HashMap;
import java.util.Map;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

@Entity(name="movie_cast")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@TypeDef(name = "json", typeClass = JsonType.class)
public class CastEntity {

    @Id
    @Column(name="movie_cast_id")
    private Long castId;

    @Type(type="json")
    @Column(name="movie_id", columnDefinition = "json")
    private Map<String, Object> movieId = new HashMap<>();

    @Column(name="name_eng")
    private String nameEng;

    @Column(name="role")
    private String role;

    public static CastEntity toEntity(MovieExternalApiDto.CastInfo cast, Long movieId){

        JSONArray movieArray = new JSONArray();
        movieArray.add(movieId);

        JSONObject object = new JSONObject();
        object.put("movieId", movieArray);

        return CastEntity.builder()
            .castId(cast.getId())
            .movieId(object)
            .nameEng(cast.getName())
            .role(cast.getKnownForDepartment())
            .build();
    }

}
