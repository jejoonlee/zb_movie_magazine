package com.jejoonlee.movmag.app.review.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class ReviewDelete {

    @Getter
    @Setter
    @Builder
    public static class Response {
        private Long reviewID;
        private String author;
        private String message;
    }
}
