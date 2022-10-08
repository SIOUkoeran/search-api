package com.example.searchapi.address.service;


import com.example.searchapi.BaseTest;
import com.example.searchapi.address.dto.SuggestAddressDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class AddressSuggestServiceTest extends BaseTest {

    @Autowired
    AddressSuggestService addressSuggestService;

    private final Logger log = LoggerFactory.getLogger(AddressSuggestServiceTest.class);

    @Test
    @DisplayName("서울특별시 자동완성 검색 기대값 = 5")
    void testSuggestAddress() {
        String address = "서울특별시";
        SuggestAddressDto.Request request = new SuggestAddressDto.Request(address);
        SuggestAddressDto.Response response = addressSuggestService.suggestAddress(request);
        response.getAddress().forEach(
                addresses -> log.info("address {}", addresses)
        );
        Assertions.assertThat(response.getAddress().size()).isEqualTo(5);
    }

    @Test
    @DisplayName("중랑구 자동완성 검색 기대값 = 1")
    void testSuggestAddress2() {
        String address = "중랑구";
        SuggestAddressDto.Request req= new SuggestAddressDto.Request(address);
        SuggestAddressDto.Response response = addressSuggestService.suggestAddress(req);
        Assertions.assertThat(response.getAddress().size()).isEqualTo(1);
        Assertions.assertThat(response.getAddress().get(0)).isEqualTo("서울특별시 중랑구 목동 385-0");
    }

}
