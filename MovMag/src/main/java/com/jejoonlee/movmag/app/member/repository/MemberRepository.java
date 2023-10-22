package com.jejoonlee.movmag.app.member.repository;

import com.jejoonlee.movmag.app.member.domain.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {

    boolean existsByEmail(String email);
}
