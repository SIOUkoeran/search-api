package com.example.searchapi.poi.service;

import com.example.searchapi.BaseTest;
import com.example.searchapi.address.dto.CreateAddressDto;
import com.example.searchapi.address.repository.AddressRepository;
import com.example.searchapi.address.service.AddressService;
import com.example.searchapi.category.exception.NotFoundPoiException;
import com.example.searchapi.poi.dto.CreatePoi;
import com.example.searchapi.poi.dto.UpdatePoi;
import com.example.searchapi.poi.model.Poi;
import com.example.searchapi.poi.repository.PoiRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

@SpringBootTest
class PoiServiceTest extends BaseTest {


    @Autowired
    PoiService poiService;

    @Autowired
    AddressService addressService;

    @Autowired
    PoiRepository poiRepository;

    @Autowired
    AddressRepository addressRepository;

    private static final List<String> mustDeletePoiId = new LinkedList<>();

    Logger log = LoggerFactory.getLogger(PoiServiceTest.class);

    @AfterEach
    void afterEach(){
        log.info("ready to delete Poi");
        mustDeletePoiId
                .forEach(poiId -> {
                    this.poiRepository.deleteById(poiId);
                    this.addressRepository.deleteById(poiId);
                });
        log.info("success delete Poi");
        mustDeletePoiId.clear();
    }

    @ParameterizedTest
    @DisplayName("하나의 poi 정보가 address, poi 인덱스에 각각 성공적으로 저장되는지 확인")
    @CsvSource({"address,서울특별시 관악구 신림동,0XFFFF,808,400,김밥천국,성안점,010,9534,4150,127.1313131,37.46961667,13123",
            "address,대구광역시 중구 북산동,0XFFFF,808,400,김밥천국,성안점,010,9534,4150,127.1313131,37.46961667,13123"})
    void testCreatePoi(String name,
                       String address,
                       String poiCode,
                       int primary,
                       int secondary,
                       String fname,
                       String cname,
                       int phoneA,
                       int phoneB,
                       int phoneC,
                       float lon,
                       float lan,
                       int zipCode) {
        CreatePoi.Request request = new CreatePoi.Request(poiCode,address, primary, secondary, -1
                , fname, cname, phoneA, phoneB, phoneC, zipCode, lon, lan);
        Poi poi = poiService.createPoi(request);
        CreateAddressDto.Response address1
                = addressService.createAddress(new CreateAddressDto.Request(request), poi.getPoi_id());
        log.info("poi {}", poi.getPoi_id());
        Assertions.assertThat(poi.getPoi_id()).isEqualTo(address1.getPoiId());

        String poiPoi_id = this.poiRepository.findById(poi.getPoi_id()).get().getPoi_id();
        String addressPoi_id = this.addressRepository.findById(address1.getPoiId()).get().getPoi_id();

        Assertions.assertThat(poiPoi_id).isEqualTo(addressPoi_id);
        mustDeletePoiId.add(poiPoi_id);
    }


    @ParameterizedTest
    @CsvSource({"address,서울특별시 관악구 신림동,0XFFFF,808,400,김밥천국,성안점,010,9534,4150,127.1313131,37.46961667,13123,김밥지옥"})
    @DisplayName("poi 도큐먼트 업데이트 테스트")
    void testUpdatePoi(String name,
                       String address,
                       String poiCode,
                       int primary,
                       int secondary,
                       String fname,
                       String cname,
                       int phoneA,
                       int phoneB,
                       int phoneC,
                       float lon,
                       float lan,
                       int zipCode,
                       String changeFname){

        CreatePoi.Request request = new CreatePoi.Request(poiCode,address, primary, secondary, -1
                , fname, cname, phoneA, phoneB, phoneC, zipCode, lon, lan);
        Poi poi = this.poiService.createPoi(request);
        UpdatePoi.Response updatedPoi = this.poiService.updatePoi(poi.getPoi_id(), new UpdatePoi.Request(poiCode,
                changeFname, cname, phoneA, phoneB, phoneC, zipCode, lon, lan));
        Poi findPoi = this.poiRepository.findById(poi.getPoi_id())
                .orElseThrow(NotFoundPoiException::new);

        Assertions.assertThat(findPoi.getFname()).isEqualTo(changeFname);
        mustDeletePoiId.add(findPoi.getPoi_id());
    }
}