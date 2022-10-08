package com.example.searchapi.address.service;

import com.example.searchapi.BaseTest;
import com.example.searchapi.address.dto.AddressDto;
import com.example.searchapi.address.repository.AddressRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

@SpringBootTest
public class AddressServiceTest extends BaseTest {

    @Autowired
    AddressService addressService;

    @Autowired
    AddressRepository addressRepository;

    Logger log = LoggerFactory.getLogger(AddressSuggestServiceTest.class);

    @AfterEach
    void afterEach() {

    }

    @ParameterizedTest
    @CsvSource({"address,서울특별시 관악구 신림동,0XFFFF,808,400"})
    @DisplayName("address 데이터 생성 후 저장")
    void createAddressAndSaveTest(String name, String address, String poiCode, int primaryBun, int secondaryBun) {

        int sanBun = 0;
        AddressDto.RequestCreate requestCreate
                = new AddressDto.RequestCreate(poiCode, address, primaryBun, secondaryBun, sanBun);
        AddressDto.ResponseCreate savedAddress = this.addressService.createAddress(requestCreate, "1");
        log.info(savedAddress.getAddress());
        Assertions.assertThat(requestCreate.getAddress()).isEqualTo(savedAddress.getAddress());
        Assertions.assertThat(requestCreate.getPoiCode()).isEqualTo(savedAddress.getPoiCode());
        Assertions.assertThat(requestCreate.getPrimaryBun()).isEqualTo(savedAddress.getPrimaryBun());
        Assertions.assertThat(requestCreate.getSecondaryBun()).isEqualTo(savedAddress.getSecondaryBun());

        this.addressRepository.deleteById(savedAddress.getPoiId());
    }
}
