package com.jejoonlee.movmag.app.reviewComment.dto;

import com.jejoonlee.movmag.app.member.domain.MemberEntity;
import com.jejoonlee.movmag.app.review.domain.ReviewEntity;
import com.jejoonlee.movmag.app.reviewComment.domain.CommentEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

public class CommentUpdate {

    @Builder
    @Getter
    @Setter
    public static class Request {
        @Positive(message="리뷰 ID를 가지고 와주세요")
        private Long reviewId;

        @Positive(message = "댓글 ID를 써주세요")
        private Long commentId;

        @NotBlank(message="리뷰에 대한 댓글을 써주세요")
        private String content;
    }

    @Getter
    @Setter
    @Builder
    public static class Response {
        private Long commentId;
        private String commentWrittenBy;
        private Long reviewId;
        private String reviewName;
        private String content;
        private LocalDateTime registeredAt;
        private LocalDateTime updatedAt;

        public static Response fromEntity(CommentEntity comment) {

            ReviewEntity reviewEntity = comment.getReviewEntity();
            MemberEntity memberEntity = comment.getMemberEntity();

            return Response.builder()
                    .commentId(comment.getCommentId())
                    .commentWrittenBy(memberEntity.getUsername())
                    .reviewId(reviewEntity.getReviewId())
                    .reviewName(reviewEntity.getReviewTitle())
                    .content(comment.getContent())
                    .registeredAt(comment.getRegisteredAt())
                    .updatedAt(comment.getUpdatedAt())
                    .build();
        }
    }
}
