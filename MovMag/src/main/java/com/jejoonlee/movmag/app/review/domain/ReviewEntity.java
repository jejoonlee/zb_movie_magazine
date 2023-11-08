package com.jejoonlee.movmag.app.review.domain;

import com.jejoonlee.movmag.app.member.domain.MemberEntity;
import com.jejoonlee.movmag.app.movie.domain.MovieEntity;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Entity(name = "movie_review")
@EntityListeners(value = AuditingEntityListener.class)
public class ReviewEntity {

    @Id
    @Column(name = "review_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @ManyToOne(fetch = FetchType.LAZY)
    private MovieEntity movieEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    private MemberEntity memberEntity;

    @Column(name="review_title", nullable = false, length = 55)
    private String reviewTitle;

    @Column(name="review_oneline", nullable = false, length = 110)
    private String reviewOneline;

    @Column(name="movie_score", nullable = false)
    private Double movieScore;

    @Column(name="content", nullable = false, length = 5100)
    private String content;

    @Column(name="registered_at", nullable = false)
    @CreatedDate
    private LocalDateTime registeredAt;

    @Column(name="updated_at")
    @LastModifiedDate
    private LocalDateTime updatedAt;

}
