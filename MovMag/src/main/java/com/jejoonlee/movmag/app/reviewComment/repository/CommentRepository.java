package com.jejoonlee.movmag.app.reviewComment.repository;

import com.jejoonlee.movmag.app.member.domain.MemberEntity;
import com.jejoonlee.movmag.app.review.domain.ReviewEntity;
import com.jejoonlee.movmag.app.reviewComment.domain.CommentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    Page<CommentEntity> findAllByMemberEntityOrderByUpdatedAtDesc(MemberEntity member, Pageable pageable);

    List<CommentEntity> findAllByReviewEntity(ReviewEntity reviewEntity);

    void deleteAllByReviewEntity(ReviewEntity reviewEntity);
}
