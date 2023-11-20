package com.jejoonlee.movmag.app.reviewComment.dto;

import com.jejoonlee.movmag.app.member.domain.MemberEntity;
import com.jejoonlee.movmag.app.review.domain.ReviewEntity;
import com.jejoonlee.movmag.app.reviewComment.domain.CommentEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class CommentDto {

    private ReviewEntity reviewEntity;
    private MemberEntity memberEntity;
    private String content;
    private LocalDateTime registeredAt;
    private LocalDateTime updatedAt;

    public CommentEntity toEntity(){
        return CommentEntity.builder()
                .reviewEntity(this.reviewEntity)
                .memberEntity(this.memberEntity)
                .content(this.content)
                .registeredAt(this.registeredAt)
                .updatedAt(this.updatedAt)
                .build();
    }
}
