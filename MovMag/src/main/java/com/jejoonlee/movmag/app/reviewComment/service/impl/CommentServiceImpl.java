package com.jejoonlee.movmag.app.reviewComment.service.impl;

import com.jejoonlee.movmag.app.member.domain.MemberRole;
import com.jejoonlee.movmag.app.member.dto.MemberDto;
import com.jejoonlee.movmag.app.review.domain.ReviewEntity;
import com.jejoonlee.movmag.app.review.repository.ReviewRepository;
import com.jejoonlee.movmag.app.reviewComment.domain.CommentEntity;
import com.jejoonlee.movmag.app.reviewComment.dto.CommentDelete;
import com.jejoonlee.movmag.app.reviewComment.dto.CommentDto;
import com.jejoonlee.movmag.app.reviewComment.dto.CommentRegister;
import com.jejoonlee.movmag.app.reviewComment.repository.CommentRepository;
import com.jejoonlee.movmag.app.reviewComment.service.CommentService;
import com.jejoonlee.movmag.exception.CommentException;
import com.jejoonlee.movmag.exception.ErrorCode;
import com.jejoonlee.movmag.exception.ReviewClientException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final ReviewRepository reviewRepository;

    private boolean authorizedUser(MemberDto memberDto, CommentEntity commentEntity) {

        MemberRole loggedInMemRole = memberDto.getRole();
        Long loggedInMemId = memberDto.getMemberId();

        Long commentMemId = commentEntity.getMemberEntity().getMemberId();

        if (loggedInMemRole == MemberRole.ADMIN) {
            return true;

        } else if (loggedInMemId == commentMemId) {
            return true;

        } else {
            throw new CommentException(ErrorCode.LOGGED_IN_USER_AND_COMMENT_USER_NOT_MATCH);
        }
    }

    @Override
    public CommentRegister.Response createComment(CommentRegister.Request request, Authentication authentication) {

        MemberDto memberDto = (MemberDto) authentication.getPrincipal();

        ReviewEntity reviewEntity = reviewRepository.findById(request.getReviewId())
                .orElseThrow(() -> new ReviewClientException(ErrorCode.REVIEW_NOT_FOUND));

        CommentDto commentDto = CommentDto.builder()
                .reviewEntity(reviewEntity)
                .memberEntity(MemberDto.toEntity(memberDto))
                .content(request.getContent())
                .registeredAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        CommentEntity commentEntity = commentDto.toEntity();

        commentRepository.save(commentEntity);

        return CommentRegister.Response.fromEntity(commentEntity);
    }

    @Override
    public CommentDelete.Response deleteComment(Long reviewId, Long commentId, Authentication authentication) {

        if (!reviewRepository.existsById(reviewId)) {
            throw new ReviewClientException(ErrorCode.REVIEW_NOT_FOUND);
        }

        CommentEntity commentEntity = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentException(ErrorCode.COMMENT_NOT_FOUND));

        MemberDto memberDto = (MemberDto) authentication.getPrincipal();

        authorizedUser(memberDto, commentEntity);

        commentRepository.delete(commentEntity);

        return CommentDelete.Response.builder()
                .commentId(commentEntity.getCommentId())
                .author(memberDto.getUsername())
                .message("댓글이 삭제가 되었습니다")
                .build();
    }
}
