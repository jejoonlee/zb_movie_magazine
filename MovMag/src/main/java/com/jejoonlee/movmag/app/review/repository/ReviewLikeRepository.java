package com.jejoonlee.movmag.app.review.repository;

import com.jejoonlee.movmag.app.member.domain.MemberEntity;
import com.jejoonlee.movmag.app.review.domain.LikeEntity;
import com.jejoonlee.movmag.app.review.domain.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewLikeRepository extends JpaRepository<LikeEntity, Long> {

    boolean existsByReviewEntityAndMemberEntity(ReviewEntity reviewEntity, MemberEntity memberEntity);

    LikeEntity findByReviewEntityAndMemberEntity(ReviewEntity reviewEntity, MemberEntity memberEntity);

}
