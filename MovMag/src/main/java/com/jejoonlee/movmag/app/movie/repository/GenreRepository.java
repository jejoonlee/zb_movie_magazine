package com.jejoonlee.movmag.app.movie.repository;

import com.jejoonlee.movmag.app.movie.domain.GenreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenreRepository extends JpaRepository<GenreEntity, Long> {
}
