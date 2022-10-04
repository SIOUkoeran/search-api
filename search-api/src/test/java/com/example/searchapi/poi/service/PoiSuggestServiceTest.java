package com.example.searchapi.poi.service;

import com.example.searchapi.BaseTest;
import com.example.searchapi.poi.dto.PoiSuggestDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.runners.Parameterized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.shaded.org.apache.commons.lang.math.IntRange;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PoiSuggestServiceTest extends BaseTest {

    @Autowired
    PoiSuggestService poiSuggestService;

    Logger log = LoggerFactory.getLogger(PoiSuggestServiceTest.class);

    @ParameterizedTest
    @ValueSource(strings = {"보성녹돈"})
    @DisplayName("S-OIL 자동완성 테스트, 기대값 1개")
    void suggestPoiNameTest(String input) {
        PoiSuggestDto.Request request = new PoiSuggestDto.Request(input);
        PoiSuggestDto.Response response = this.poiSuggestService.suggestPoi(request);
        Assertions.assertThat(response.getSuggestNameList().size()).isEqualTo(1);
        log.info("response result {}", response.getSuggestNameList().get(0));
    }

    @ParameterizedTest
    @ValueSource(strings = {"오시오"})
    @DisplayName("오시오 자동완성 테스트, 기대값 5개")
    void suggestPoiNameTest2(String input) {
        PoiSuggestDto.Request request = new PoiSuggestDto.Request(input);
        PoiSuggestDto.Response response = this.poiSuggestService.suggestPoi(request);
        Assertions.assertThat(response.getSuggestNameList().size()).isEqualTo(5);
        String expectedResult = "오시오상회";
        AtomicInteger i = new AtomicInteger(1);
        List<String> suggestNameList = response.getSuggestNameList();
        IntStream.range(1,5)
                        .forEach(idx ->
                                Assertions.assertThat(suggestNameList.get(idx))
                                        .isEqualTo(expectedResult + String.valueOf(idx) + " ")
                        )
        ;
    }
}