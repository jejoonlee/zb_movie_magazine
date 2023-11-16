package com.jejoonlee.movmag.app.review.repository;

import com.jejoonlee.movmag.app.member.domain.MemberEntity;
import com.jejoonlee.movmag.app.review.domain.LikeEntity;
import com.jejoonlee.movmag.app.review.domain.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewLikeRepository extends JpaRepository<LikeEntity, Long> {

    boolean existsByReviewEntityAndMemberEntity(ReviewEntity reviewEntity, MemberEntity memberEntity);

    LikeEntity findByReviewEntityAndMemberEntity(ReviewEntity reviewEntity, MemberEntity memberEntity);

    List<LikeEntity> findAllByReviewEntity(ReviewEntity reviewEntity);

}
