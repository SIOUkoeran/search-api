package com.example.searchapi.address.api;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.searchapi.BaseTest;
import com.example.searchapi.address.dto.CreateAddressDto;
import com.example.searchapi.address.dto.UpdateAddress;
import com.example.searchapi.address.exception.NotFoundAddressException;
import com.example.searchapi.address.model.Address;
import com.example.searchapi.address.repository.AddressRepository;
import com.example.searchapi.address.service.AddressService;
import com.example.searchapi.poi.repository.PoiRepository;
import java.util.LinkedList;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;


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
                this.addressRepository.deleteByPoiId(poiId);
                this.poiRepository.deleteByPoiId(poiId);
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

        CreateAddressDto.Request request = new CreateAddressDto.Request(poiCode, address, primary,
            secondary, sanbun);

        UpdateAddress.Request updateRequest = new UpdateAddress.Request(poiCode, changeAddress,
            primary, secondary, sanbun);

        ResultActions resultActions = mockMvc.perform(put("/api/address")
                .param("id", "123")
                .content(objectMapper.writeValueAsString(updateRequest))
                .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().is(204))
            .andExpect(jsonPath("address").exists());

        String contentAsString = resultActions.andReturn().getResponse().getContentAsString();

        Address address1 = this.addressRepository.findAddressByPoiId("123")
            .orElseThrow(NotFoundAddressException::new);
        Assertions.assertThat(address1).isNotNull();
        Assertions.assertThat(address1.getAddress()).isEqualTo(changeAddress);

        mustDeletePoiId.add(address1.getPoiId());
    }
}
