package com.jejoonlee.movmag.app.movie.repository;

import com.jejoonlee.movmag.app.movie.domain.CastEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CastRepository extends JpaRepository<CastEntity, Long> {
}
