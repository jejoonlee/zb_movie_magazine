package com.jejoonlee.movmag.app.movie.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.HashSet;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity(name="cast")
public class CastEntity {

    @Id
    @Column(name="cast_id")
    private Long castId;

    @Column(name="movie_id")
    @Convert(converter = LongSetConverter.class)
    private HashSet<Long> movie = new HashSet<>();

    @Column(name="name_eng")
    private String nameEng;

    @Column(name="role")
    private String role;

}
