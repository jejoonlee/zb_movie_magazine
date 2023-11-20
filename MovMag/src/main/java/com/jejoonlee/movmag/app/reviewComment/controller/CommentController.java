package com.jejoonlee.movmag.app.reviewComment.controller;

import com.jejoonlee.movmag.app.reviewComment.dto.CommentDelete;
import com.jejoonlee.movmag.app.reviewComment.dto.CommentDetail;
import com.jejoonlee.movmag.app.reviewComment.dto.CommentRegister;
import com.jejoonlee.movmag.app.reviewComment.dto.CommentUpdate;
import com.jejoonlee.movmag.app.reviewComment.service.CommentService;
import com.jejoonlee.movmag.exception.ErrorCode;
import com.jejoonlee.movmag.exception.MemberException;
import io.swagger.annotations.ApiOperation;
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
    @ApiOperation(value="댓글을 작성한다. 로그인을 해야 한다")
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
    @ApiOperation(value="댓글을 수정한다. Admin은 댓글이 부적절하다고 생각하면, 모든 댓글을 수정할 수 있다")
    public CommentUpdate.Response createComment(
            @RequestBody CommentUpdate.Request request,
            Authentication authentication
    ) {
        return commentService.updateComment(request, authentication);
    }


    // 리뷰 댓글 삭제
    // http://localhost:8080/review/comment/delete?reviewId={}&commentId={}
    // commentId, 리뷰ID, authentication
    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EDITOR', 'ROLE_USER')")
    @ApiOperation(value="댓글을 삭제한다. Admin은 댓글이 부적절하다고 생각하면, 댓글을 삭제할 수 있다")
    public CommentDelete.Response createComment(
            @RequestParam Long reviewId,
            @RequestParam Long commentId,
            Authentication authentication
    ) {
        return commentService.deleteComment(reviewId, commentId, authentication);
    }

    // 리뷰 댓글 확인
    // http://localhost:8080/review/comment?page={pageNum}
    @GetMapping("")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EDITOR', 'ROLE_USER')")
    @ApiOperation(value="내가 쓴 모든 댓글을 볼 수 있다")
    public CommentDetail.PageInfo getComment(
            @RequestParam int page,
            Authentication authentication
    ) {
        if (!authentication.isAuthenticated())
            throw new MemberException(ErrorCode.USER_PERMISSION_NOT_GRANTED);

        return commentService.getComment(page, authentication);
    }

}
