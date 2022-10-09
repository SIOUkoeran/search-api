package com.example.searchapi.address.api;


import com.example.searchapi.BaseTest;
import com.example.searchapi.address.dto.CreateAddressDto;
import com.example.searchapi.address.dto.UpdateAddress;
import com.example.searchapi.address.repository.AddressRepository;
import com.example.searchapi.address.service.AddressService;
import com.example.searchapi.poi.repository.PoiRepository;
import org.apache.http.HttpHeaders;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
public class AddressControllerTest extends BaseTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    AddressService addressService;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    PoiRepository poiRepository;

    ObjectMapper objectMapper = new ObjectMapper();
    private static final List<String> mustDeletePoiId = new LinkedList<>();
    @AfterEach
    void afterEach() {
            mustDeletePoiId
                    .forEach(poiId -> {
                        this.addressRepository.deleteById(poiId);
                        this.poiRepository.deleteById(poiId);
                    });
    }

    @ParameterizedTest
    @CsvSource({"address,서울특별시 관악구 신림동,0XFFFF,808,400,0,서울특별시 종로구 목동"})
    @DisplayName("address update api 테스트")
    void testUpdateAddress(String name,
                           String address,
                           String poiCode,
                           int primary,
                           int secondary,
                           int sanbun,
                           String changeAddress) throws Exception {

        CreateAddressDto.Request request = new CreateAddressDto.Request(poiCode, address, primary, secondary, sanbun);
        CreateAddressDto.Response saveResponse = this.addressService.createAddress(request, "10");

        UpdateAddress.Request updateRequest = new UpdateAddress.Request(poiCode, changeAddress, primary, secondary, sanbun);


        mockMvc.perform(put("/api/address")
                .param("id", "10")
                .content(objectMapper.writeValueAsString(updateRequest))
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().is(204))
                .andExpect(jsonPath("address").exists())
                .andExpect(jsonPath("address.address_suggest").exists())
                .andExpect(jsonPath("address.address_suggest.input").isArray())

        ;
        Assertions.assertThat(this.addressRepository.existsById("10")).isTrue();
        Assertions.assertThat(this.addressRepository.findById("10").get().getAddress()).isEqualTo(changeAddress);
        mustDeletePoiId.add("10");
    }
}
