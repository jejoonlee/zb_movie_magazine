package com.jejoonlee.movmag.app.movie.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name="genre")
public class GenreEntity {

    @Id
    @Column(name="genre_id")
    private Long genreId;

    @Column(name="name_eng")
    private String nameEng;

    @Column(name="name_kor")
    private String nameKor;

}
