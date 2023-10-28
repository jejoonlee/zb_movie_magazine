package com.jejoonlee.movmag.app.movie.dto;

import lombok.Getter;
import lombok.Setter;

public class UpdateMovie {

    @Getter
    @Setter
    public class Request {
        private String apiKey;
    }

    @Getter
    @Setter
    public class Response {
        private int movieNum;
        private int peopleNum;
    }
}
