package com.jejoonlee.movmag.app.movie.domain;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.List;

@Entity(name="cast")
public class CastEntity {

    @Id
    @Column(name="cast_id")
    private Long castId;

    @Column(name="movie_id")
    @Convert(converter = LongArrayConverter.class)
    private List<Long> movie;

    @Column(name="name_eng")
    private String nameEng;

    @Column(name="role")
    private String role;

}
