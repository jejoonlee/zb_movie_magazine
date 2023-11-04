package com.jejoonlee.movmag.app.movie.domain;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import lombok.AllArgsConstructor;
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

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity(name="cast")
@TypeDef(name = "json", typeClass = JsonType.class)
public class CastEntity {

    @Id
    @Column(name="cast_id")
    private Long castId;

    @Type(type="json")
    @Column(name="movie_id", columnDefinition = "json")
    private Map<String, Object> movieId = new HashMap<>();

    @Column(name="name_eng")
    private String nameEng;

    @Column(name="role")
    private String role;

}
