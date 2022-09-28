package com.example.searchapi.address.service;


import com.example.searchapi.BaseTest;
import com.example.searchapi.address.dto.SuggestAddressDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class AddressSuggestServiceTest extends BaseTest {

    @Autowired
    AddressSuggestService addressSuggestService;

    @Test
    void testSuggestAddress() {
        String address = "서울특별시";
        SuggestAddressDto.Request request = new SuggestAddressDto.Request(address);
        SuggestAddressDto.Response response = addressSuggestService.suggestAddress(request);

        assertThat(response.getAddress().size()).isEqualTo(5);
    }
}
