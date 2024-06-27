package com.hyeongarl.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class TreeConverter implements AttributeConverter<Map, String> {

    private final ObjectMapper objectMapper;

    @Autowired
    public TreeConverter(@Lazy ObjectMapper objectMapper) {
        // Bean으로 등록한 ObjectMapper와 순환참조 문제를 해결하기 위해 @Lazy 사용
        this.objectMapper = objectMapper;
    }

    @Override
    public String convertToDatabaseColumn(Map map) {
        try {
            return objectMapper.writeValueAsString(map);
        } catch(JsonProcessingException e) {
            throw new IllegalArgumentException("Map을 JSON으로 변환 중 오류가 발생했습니다.", e);
        }
    }

    @Override
    public Map convertToEntityAttribute(String data) {
        try {
            return objectMapper.readValue(data, Map.class);
        } catch (IOException e) {
            throw new IllegalArgumentException("JSON을 Map으로 변환 중 오류가 발생했습니다.", e);
        }
    }
}
