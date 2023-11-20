package com.jejoonlee.movmag.app.reviewComment.domain;

import com.jejoonlee.movmag.app.member.domain.MemberEntity;
import com.jejoonlee.movmag.app.review.domain.ReviewEntity;
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
@Entity(name = "review_comment")
@EntityListeners(value = AuditingEntityListener.class)
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @ManyToOne(fetch = FetchType.LAZY)
    private ReviewEntity reviewEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    private MemberEntity memberEntity;

    @Column(name="content", nullable = false, length = 400)
    private String content;

    @Column(name="registered_at", nullable = false)
    @CreatedDate
    private LocalDateTime registeredAt;

    @Column(name="updated_at")
    @LastModifiedDate
    private LocalDateTime updatedAt;

}
