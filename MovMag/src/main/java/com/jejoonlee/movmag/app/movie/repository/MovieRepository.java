package com.jejoonlee.movmag.app.movie.repository;

import com.jejoonlee.movmag.app.movie.domain.MovieEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends JpaRepository<MovieEntity, Long> {
}
