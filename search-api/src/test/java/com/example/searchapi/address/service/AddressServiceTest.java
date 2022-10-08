package com.example.searchapi.address.service;

import com.example.searchapi.BaseTest;
import com.example.searchapi.address.dto.CreateAddressDto;
import com.example.searchapi.address.dto.UpdateAddress;
import com.example.searchapi.address.exception.NotFoundAddressException;
import com.example.searchapi.address.model.Address;
import com.example.searchapi.address.repository.AddressRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class AddressServiceTest extends BaseTest {

    @Autowired
    AddressService addressService;

    @Autowired
    AddressRepository addressRepository;

    Logger log = LoggerFactory.getLogger(AddressSuggestServiceTest.class);

    private static final List<String> mustDeletePoiId = new LinkedList<>();

    @AfterEach
    void afterEach() {
        log.info("ready to delete address Poi");
        mustDeletePoiId.forEach(
                poiId -> this.addressRepository.deleteById(poiId)
        );
        log.info("success to delete addres Poi");
    }

    @ParameterizedTest
    @CsvSource({"address,서울특별시 관악구 신림동,0XFFFF,808,400"})
    @DisplayName("address 데이터 생성 후 저장")
    void createAddressAndSaveTest(String name, String address, String poiCode, int primaryBun, int secondaryBun) {

        int sanBun = 0;
        CreateAddressDto.Request request
                = new CreateAddressDto.Request(poiCode, address, primaryBun, secondaryBun, sanBun);
        CreateAddressDto.Response savedAddress = this.addressService.createAddress(request, "1");
        log.info(savedAddress.getAddress());
        Assertions.assertThat(request.getAddress()).isEqualTo(savedAddress.getAddress());
        Assertions.assertThat(request.getPoiCode()).isEqualTo(savedAddress.getPoiCode());
        Assertions.assertThat(request.getPrimaryBun()).isEqualTo(savedAddress.getPrimaryBun());
        Assertions.assertThat(request.getSecondaryBun()).isEqualTo(savedAddress.getSecondaryBun());

        this.addressRepository.deleteById(savedAddress.getPoiId());
        mustDeletePoiId.add(savedAddress.getPoiId());
    }

    @ParameterizedTest
    @DisplayName("address 삭제 업데이트 테스트")
    @CsvSource({"address,서울특별시 관악구 신림동,0XFFFF,808,400"})
    void testDeleteAddressDocument(String name, String address, String poiCode, int primaryBun, int secondaryBun) {
        int sanBun = 0;
        CreateAddressDto.Request request
                = new CreateAddressDto.Request(poiCode, address, primaryBun, secondaryBun, sanBun);
        CreateAddressDto.Response savedAddress = this.addressService.createAddress(request, "2");
        Address find = this.addressRepository.findById(savedAddress.getPoiId())
                .orElseThrow(NotFoundAddressException::new);
        this.addressRepository.deleteById(find.getPoi_id());
        Assertions.assertThat(this.addressRepository.existsById("2")).isFalse();
    }

    @ParameterizedTest
    @DisplayName("address 도큐먼트 업데이트 테스트")
    @CsvSource({"address,서울특별시 관악구 신림동,0XFFFF,808,400,서울특별시 종로구 목동"})
    void testUpdateAddressDocument(String name, String address, String poiCode,
                                   int primaryBun, int secondaryBun, String changeAddress) {
        int sanBun = 0;
        CreateAddressDto.Request request
                = new CreateAddressDto.Request(poiCode, address, primaryBun, secondaryBun, sanBun);
        CreateAddressDto.Response savedAddress = this.addressService.createAddress(request, "1");

        Address find = this.addressRepository.findById("1")
                .orElseThrow(NotFoundAddressException::new);

        UpdateAddress.Request updateRequest = new UpdateAddress.Request(
                poiCode, changeAddress,primaryBun, secondaryBun, sanBun
        );
        UpdateAddress.Response response = addressService.updateAddress("1", updateRequest);

        Assertions.assertThat(response.getAddress().getAddress()).isEqualTo(changeAddress);
        mustDeletePoiId.add("1");
    }
}
