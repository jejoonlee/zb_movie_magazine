package com.jejoonlee.movmag.app.reviewComment.repository;

import com.jejoonlee.movmag.app.reviewComment.domain.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
}
