package com.jejoonlee.movmag.app.reviewComment.service.impl;

import com.jejoonlee.movmag.app.member.domain.MemberRole;
import com.jejoonlee.movmag.app.member.dto.MemberDto;
import com.jejoonlee.movmag.app.review.domain.ReviewEntity;
import com.jejoonlee.movmag.app.review.repository.ReviewRepository;
import com.jejoonlee.movmag.app.reviewComment.domain.CommentEntity;
import com.jejoonlee.movmag.app.reviewComment.dto.*;
import com.jejoonlee.movmag.app.reviewComment.repository.CommentRepository;
import com.jejoonlee.movmag.app.reviewComment.service.CommentService;
import com.jejoonlee.movmag.exception.CommentException;
import com.jejoonlee.movmag.exception.ErrorCode;
import com.jejoonlee.movmag.exception.ReviewClientException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final ReviewRepository reviewRepository;

    private boolean authorizedUser(MemberDto memberDto, CommentEntity commentEntity) {

        MemberRole loggedInMemRole = memberDto.getRole();
        Long loggedInMemId = memberDto.getMemberId();

        Long commentMemId = commentEntity.getMemberEntity().getMemberId();

        // 어드민, 또는 댓글을 쓴 사람과, 로그인한 사람이 일치하면 같으면 수정 또는 삭제를 할 수 있음
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

    @Override
    public CommentUpdate.Response updateComment(CommentUpdate.Request request, Authentication authentication) {

        ReviewEntity reviewEntity = reviewRepository.findById(request.getReviewId())
                .orElseThrow(() -> new ReviewClientException(ErrorCode.REVIEW_NOT_FOUND));

        CommentEntity commentEntity = commentRepository.findById(request.getCommentId())
                .orElseThrow(() -> new CommentException(ErrorCode.COMMENT_NOT_FOUND));

        MemberDto memberDto = (MemberDto) authentication.getPrincipal();

        authorizedUser(memberDto, commentEntity);

        commentEntity.setReviewEntity(reviewEntity);
        commentEntity.setContent(request.getContent());

        commentRepository.save(commentEntity);

        return CommentUpdate.Response.fromEntity(commentEntity);
    }

    @Override
    public CommentDetail.PageInfo getComment(int page, Authentication authentication) {

        MemberDto memberDto = (MemberDto) authentication.getPrincipal();

        Page<CommentEntity> commentPage = commentRepository.findAllByMemberEntityOrderByUpdatedAtDesc( MemberDto.toEntity(memberDto),
                PageRequest.of(page - 1, 20));

        List<CommentDetail.Response> commentList = commentPage.getContent().stream()
                .map(CommentDetail.Response::fromEntity)
                .collect(Collectors.toList());

        return CommentDetail.PageInfo.builder()
                .page(page)
                .totalPage(commentPage.getTotalPages())
                .foundDataNum(commentPage.getTotalElements())
                .data(commentList)
                .build();
    }
}
