package com.example.searchapi.common.query;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class QueryPageUtils {

    public static<T> Page<T> convertListToPage(List<T> list, int page, int size) {
        int start = Math.min(size * page, list.size());
        int end = Math.min(size * (page + 1), list.size());
        log.info("start : {} end {}", start, end);
        return new PageImpl<>(list.subList(start, end), PageRequest.of(page, size), list.size());
    }
}
