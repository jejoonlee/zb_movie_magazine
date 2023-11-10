package com.jejoonlee.movmag.app.elasticsearch.document;

import com.jejoonlee.movmag.app.review.dto.ReviewDetail;
import lombok.*;
import org.springframework.data.elasticsearch.annotations.*;
import org.springframework.data.annotation.Id;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Setting(settingPath = "static/elastic/elastic-settings.json")
@Mapping(mappingPath = "static/elastic/review-mapping.json")
@Document(indexName = "review_index") // 정해줄 index (table) 이름
public class ReviewDocument {

    @Id
    @Field(name = "review_id", type = FieldType.Keyword)
    private Long reviewId;

    @Field(name = "movie_title_eng", type = FieldType.Text)
    private String movieTitleEng;

    @Field(name = "movie_title_kor", type = FieldType.Text)
    private String movieTitleKor;

    @Field(name = "author", type = FieldType.Text)
    private String author;

    @Field(name = "review_title", type = FieldType.Text)
    private String reviewTitle;

    @Field(name = "review_oneline", type = FieldType.Text)
    private String reviewOneline;

    @Field(name = "movie_score", type = FieldType.Double)
    private Double movieScore;

    @Field(name = "updated_at", type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime updatedAt;

    public static ReviewDocument fromReviewDetail(ReviewDetail reviewDetail) {
        return ReviewDocument.builder()
                .reviewId(reviewDetail.getReviewId())
                .movieTitleEng(reviewDetail.getMovieTitleEng())
                .movieTitleKor(reviewDetail.getMovieTitleKor())
                .author(reviewDetail.getAuthor())
                .reviewTitle(reviewDetail.getReviewTitle())
                .reviewOneline(reviewDetail.getReviewOneline())
                .movieScore(reviewDetail.getMovieScore())
                .updatedAt(reviewDetail.getUpdatedAt())
                .build();
    }
}
