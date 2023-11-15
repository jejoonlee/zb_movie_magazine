package com.jejoonlee.movmag.app.reviewComment.controller;

import com.jejoonlee.movmag.app.reviewComment.dto.CommentDelete;
import com.jejoonlee.movmag.app.reviewComment.dto.CommentDetail;
import com.jejoonlee.movmag.app.reviewComment.dto.CommentRegister;
import com.jejoonlee.movmag.app.reviewComment.dto.CommentUpdate;
import com.jejoonlee.movmag.app.reviewComment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/review/comment")
public class CommentController {

    private final CommentService commentService;

    // 리뷰 댓글 작성
    // http://localhost:8080/review/comment/register
    @PostMapping("/register")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EDITOR', 'ROLE_USER')")
    public CommentRegister.Response createComment(
            @RequestBody @Valid CommentRegister.Request request,
            Authentication authentication
    ) {
        return commentService.createComment(request, authentication);
    }

    // 리뷰 댓글 수정
    // http://localhost:8080/review/comment/update
    // commentId, 리뷰ID, authentication
    @PutMapping("/update")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EDITOR', 'ROLE_USER')")
    public CommentUpdate.Response createComment(
            @RequestBody CommentUpdate.Request request,
            Authentication authentication
    ) {
        return commentService.updateComment(request, authentication);
    }


    // 리뷰 댓글 삭제
    // http://localhost:8080/review/comment/delete
    // commentId, 리뷰ID, authentication
    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EDITOR', 'ROLE_USER')")
    public CommentDelete.Response createComment(
            @RequestParam Long reviewId,
            @RequestParam Long commentId,
            Authentication authentication
    ) {
        return commentService.deleteComment(commentId, reviewId, authentication);
    }

    // 리뷰 댓글 확인
    // http://localhost:8080/review/comment
    @GetMapping("")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EDITOR', 'ROLE_USER')")
    public CommentDetail.PageInfo getComment(
            @RequestParam int page,
            Authentication authentication
    ) {
        return commentService.getComment(page, authentication);
    }

}
