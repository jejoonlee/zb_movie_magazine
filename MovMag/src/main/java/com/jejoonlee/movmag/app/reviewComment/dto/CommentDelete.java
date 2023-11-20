package com.jejoonlee.movmag.app.reviewComment.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class CommentDelete {

    @Getter
    @Setter
    @Builder
    public static class Response {
        private Long commentId;
        private String author;
        private String message;
    }
}
