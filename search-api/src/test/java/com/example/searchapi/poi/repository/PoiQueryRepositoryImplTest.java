package com.example.searchapi.poi.repository;

import com.example.searchapi.BaseTest;
import com.example.searchapi.category.service.CategoryService;
import com.example.searchapi.poi.model.Poi;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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

    @Autowired
    CategoryService categoryService;

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
        List<String> poiCodes = List.of("0x2FFF");
        List<Poi> poiResult = this.poiQueryRepository.searchPoiByNameFilterPoiCodes(name, poiCodes, PageRequest.of(0, 20));
        List<Poi> pois = this.poiQueryRepository.searchPoiByName(name, PageRequest.of(0, 10));

        Assertions.assertThat(poiResult.size()).isEqualTo(5);
        Assertions.assertThat(poiResult.size()).isEqualTo(pois.size());
    }

    @Test
    @DisplayName("카테고리 검색 후 필터 검색")
    void testPoiNameWithFilterCategoryService() {
        List<String> poiCodes = this.categoryService.searchPoiCodesByCategory("쇼핑", "large_category");
        String name = "오시오장";
        List<Poi> pois
                = this.poiQueryRepository.searchPoiByNameFilterPoiCodes(name, poiCodes, PageRequest.of(0, 10));
        pois.forEach(
                poi -> log.info("poi : {}", poi.getFname())
        );

        Assertions.assertThat(pois.size()).isEqualTo(5);
        pois.forEach(
                poi -> Assertions.assertThat(poi.getFname().contains("오시오")).isTrue()
        );
    }
}