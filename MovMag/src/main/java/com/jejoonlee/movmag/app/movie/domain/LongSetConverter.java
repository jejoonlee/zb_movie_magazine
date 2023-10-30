package com.jejoonlee.movmag.app.movie.domain;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.HashSet;

@Converter
public class LongSetConverter implements AttributeConverter<HashSet<Long>, String> {

    private static final String SPLIT_CHAR = ",";

    // DB에 저장할 때에는 ","을 값들 사이에 넣어 String 형태로 바꿔 DB에 ㅈ장한다
    @Override
    public String convertToDatabaseColumn(HashSet<Long> attribute) {

        StringBuilder setToString = new StringBuilder();

        for (Long num : attribute) {
            setToString.append(String.valueOf(num));
            setToString.append(SPLIT_CHAR);
        }

        if (setToString.length() > 0) {
            setToString.setLength(setToString.length() - SPLIT_CHAR.length()); // Remove the trailing delimiter
        }

        return setToString.toString();
    }


    // DB에서 꺼낼 때에는 리스트 형태로 값을 리턴해준다
    @Override
    public HashSet<Long> convertToEntityAttribute(String dbData) {
        String[] stringToSet = dbData.split(SPLIT_CHAR);
        HashSet<Long> set = new HashSet<>();

        for (String str : stringToSet) set.add(Long.parseLong(str));

        return set;
    }
}