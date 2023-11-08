package com.jejoonlee.movmag.app.review.domain;

import com.jejoonlee.movmag.app.member.domain.MemberEntity;
import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Entity(name = "review_like")
public class LikeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long likeId;

    @ManyToOne(fetch = FetchType.LAZY)
    private ReviewEntity reviewEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    private MemberEntity memberEntity;
}
