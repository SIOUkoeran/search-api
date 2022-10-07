package com.example.searchapi.poi.repository;

import com.example.searchapi.BaseTest;
import com.example.searchapi.poi.model.Poi;
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

}