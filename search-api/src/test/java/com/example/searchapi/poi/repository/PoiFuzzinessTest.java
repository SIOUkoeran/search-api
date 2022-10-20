package com.example.searchapi.poi.repository;

import com.example.searchapi.BaseTest;
import com.example.searchapi.poi.model.Poi;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

import java.util.List;

@SpringBootTest
public class PoiFuzzinessTest extends BaseTest {

    Logger log = LoggerFactory.getLogger(PoiFuzzinessTest.class);

    @Autowired
    PoiQueryRepository poiQueryRepository;

    @ParameterizedTest
    @CsvSource({"오시호,오시오상회", "김밥연국,김밥천국", "김밥연국 성화점,김밥천국", "오시오, 오시오상회", "오시오 상회,오시오상회", "오시오상회,오시오상김밥연국 "})
    @DisplayName("오타보정테스트 fname 검증")
    void testFuzzyFnameTest(String input,
                       String expectedResult) {

        List<Poi> results = poiQueryRepository.searchPoiByName(input, PageRequest.of(0, 20));
        log.info("Result {}", results.get(0).getFname());
        Poi findPoi = results.get(0);
        Assertions.assertThat(findPoi.getFname()).isEqualTo(expectedResult);
    }

    @ParameterizedTest
    @CsvSource({"김밥연국 성와점,성안점", "김밥연국 성화점,성화점", "맘김밥천국 성화점, 성간점"})
    @DisplayName("오타보정테스트 cnam 검증")
    void testFuzzyCnameTest(String input,
                            String expectedResult) {
        log.info("query input {}", input);
        List<Poi> results = poiQueryRepository.searchPoiByName(input, PageRequest.of(0, 20));
        Poi findPoi = results.get(0);
        Assertions.assertThat(findPoi.getCname()).isEqualTo(expectedResult);
    }

}
