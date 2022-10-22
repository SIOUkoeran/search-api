package com.example.searchapi.address.service;


import com.example.searchapi.BaseTest;
import com.example.searchapi.address.dto.AddressDto;
import com.example.searchapi.address.dto.SuggestAddressDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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

    @ParameterizedTest
    @CsvSource({"중랑구, 서울특별시 중랑구 묵동 385-0,1", "관악구, 서울특별시 관악구 신림동,5","서울, 서울특별시, 5"})
    @DisplayName("중랑구 자동완성 검색 기대값 = 1")
    void testSuggestAddress2(
            String input,
            String expectedResult,
            int expectedSize
    ) {
        SuggestAddressDto.Request req= new SuggestAddressDto.Request(input);
        SuggestAddressDto.Response response = addressSuggestService.suggestAddress(req);
        Assertions.assertThat(response.getAddress().size()).isEqualTo(expectedSize);
        response.getAddress()
                .forEach(
                        a -> {
                            Assertions.assertThat(a).startsWith(expectedResult);
                            log.info("address {} ", a);
                        }
                );
    }

    @ParameterizedTest
    @CsvSource({"중ㄹ,서울특별시 중랑구 묵동,1"})
    @DisplayName("자음 모음 분리되어 입력되는지 확인")
    void testJasoFilterSuggest(
            String input,
            String expectedResult,
            int expectedSize
    ) {
        SuggestAddressDto.Request req= new SuggestAddressDto.Request(input);
        SuggestAddressDto.Response response = addressSuggestService.suggestAddress(req);
        Assertions.assertThat(response.getAddress().size()).isEqualTo(expectedSize);
        response.getAddress()
                .forEach(
                        a -> Assertions.assertThat(a).startsWith(expectedResult)
                );
    }
//
//    @ParameterizedTest
//    @CsvSource({"중ㄹ, 서울특별시 중랑구 "})
//    @DisplayName("자동완성 테스트")
//    void testSuggestAddressParameter(
//            String address,
//            String expectedResult
//    )

}
