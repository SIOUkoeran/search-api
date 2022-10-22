package com.example.searchapi.poi.repository;

import com.example.searchapi.BaseTest;
import com.example.searchapi.category.service.CategoryService;
import com.example.searchapi.poi.model.Poi;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class PoiQueryRepositoryImplTest extends BaseTest {


    @Autowired
    PoiQueryRepository poiQueryRepository;

    final Logger log = LoggerFactory.getLogger(PoiQueryRepositoryImplTest.class);

    @Test
    @DisplayName("오타보정 테스트 fuzziness 2 일 때")
    void testLevenshteinDistanceInAUTO() {
        String name = "오시오장";
        List<Poi> pois = this.poiQueryRepository.searchPoiByName(name, PageRequest.of(0, 10));
        assertThat(pois.size()).isEqualTo(5);
        pois
                .forEach(poi -> log.info("poi : {}", poi.getFname()));
    }

    @Test
    @DisplayName("카테고리 필터 검색 테스트")
    void testPoiNameWithFilterCategory() {
        String name = "오시오장";
        String field = "large_category";
        String category = "쇼핑";
        List<Poi> poiResult = this.poiQueryRepository.searchPoiByNameFilterPoiCodes(name, field, category, PageRequest.of(0, 20));
        List<Poi> pois = this.poiQueryRepository.searchPoiByName(name, PageRequest.of(0, 10));

        Assertions.assertThat(poiResult.size()).isEqualTo(5);
        Assertions.assertThat(poiResult.size()).isEqualTo(pois.size());
    }

    @ParameterizedTest
    @CsvSource({"오시오,large_category,쇼핑","오시오,medium_category,기타", "오시오,small_category,기타"})
    @DisplayName("poi 이름 필터 검색")
    void testPoiNameWithFilterCategoryService() {
        String name = "오시오";
        String field = "large_category";
        String category = "쇼핑";
        List<Poi> pois
                = this.poiQueryRepository.searchPoiByNameFilterPoiCodes(name, field, category, PageRequest.of(0, 10));
        pois.forEach(
                poi -> log.info("poi : {}", poi.getFname())
        );

        Assertions.assertThat(pois.size()).isEqualTo(5);
        pois.forEach(
                poi -> Assertions.assertThat(poi.getFname().contains("오시오")).isTrue()
        );
    }

    @Test
    @DisplayName("poi 이름 필터 검색 실패")
    void testPoiNameWithFilterCategoryReturnNothing() {
        String name = "오시오";
        String field = "large_category";
        String category = "식사";
        List<Poi> pois =
                this.poiQueryRepository.searchPoiByNameFilterPoiCodes(name, field, category, PageRequest.of(0, 10));
        pois.forEach(
                poi -> log.info("poi : {}", poi.getFname())
        );

        Assertions.assertThat(pois.isEmpty()).isTrue();
    }

    @ParameterizedTest
    @CsvSource({"오시오장,large_category,쇼핑,오시오", "김밥천국,large_category,식사,김밥천국", "김밥연국,large_category,식사,김밥천국"})
    @DisplayName("poi 필터 보정 테스트")
    void testPoiNameSearchWithLargeCategoryFilterAndFuzzy(
            String fname,
            String categoryField,
            String category,
            String expectedFname
    ) {
        List<Poi> pois
                = this.poiQueryRepository.searchPoiByNameFilterPoiCodes(fname, categoryField, category, PageRequest.of(0, 10));

        pois.forEach(
                poi -> log.info("poi {}", poi.getFname())
        );
        pois.forEach(
                poi -> {
                    Assertions.assertThat(poi.getFname()).contains(expectedFname);
                    Assertions.assertThat(poi.getLarge_category()).isEqualTo(category);
                }
        );
    }
}