package com.jejoonlee.movmag.app.reviewComment.service;

import com.jejoonlee.movmag.app.reviewComment.dto.CommentDelete;
import com.jejoonlee.movmag.app.reviewComment.dto.CommentRegister;
import org.springframework.security.core.Authentication;

public interface CommentService {

    CommentRegister.Response createComment(CommentRegister.Request request, Authentication authentication);

    CommentDelete.Response deleteComment(Long reviewId, Long commentId, Authentication authentication);
}
