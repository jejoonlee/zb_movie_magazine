package com.jejoonlee.movmag.app.movie.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class UpdateMovie {

    @Getter
    @Setter
    public static class Request {
        private String apiKey;
    }

    @Getter
    @Setter
    @Builder
    public static class Response {
        private int movieNum;
        private int peopleNum;
    }
}
