package com.jejoonlee.movmag.app.movie.domain;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Converter
public class LongArrayConverter implements AttributeConverter<List<Long>, String> {

    private static final String SPLIT_CHAR = ",";

    // DB에 저장할 때에는 ","을 값들 사이에 넣어 String 형태로 바꿔 DB에 ㅈ장한다
    @Override
    public String convertToDatabaseColumn(List<Long> attribute) {
        return attribute.stream().map(String::valueOf).collect(Collectors.joining(SPLIT_CHAR));
    }

    // DB에서 꺼낼 때에는 리스트 형태로 값을 리턴해준다
    @Override
    public List<Long> convertToEntityAttribute(String dbData) {
        return Arrays.stream(dbData.split(SPLIT_CHAR))
                .map(Long::parseLong)
                .collect(Collectors.toList());

    }
}