package com.example.searchapi.poi.api;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.searchapi.BaseTest;
import com.example.searchapi.address.repository.AddressRepository;
import com.example.searchapi.poi.dto.CreatePoi;
import com.example.searchapi.poi.dto.UpdatePoi;
import com.example.searchapi.poi.model.Poi;
import com.example.searchapi.poi.repository.PoiRepository;
import com.example.searchapi.poi.service.PoiService;
import com.jayway.jsonpath.JsonPath;
import java.util.LinkedList;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

@AutoConfigureMockMvc
@AutoConfigureRestDocs
@SpringBootTest
class PoiControllerTest extends BaseTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    PoiRepository poiRepository;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    PoiService poiService;

    ObjectMapper objectMapper = new ObjectMapper();

    private static final List<String> mustDeletePoiId = new LinkedList<>();

    Logger log = LoggerFactory.getLogger(PoiControllerTest.class);

    @AfterEach
    void afterEach() {
        mustDeletePoiId
            .forEach(poiId -> {
                poiRepository.deleteByPoiId(poiId);
                addressRepository.deleteByPoiId(poiId);
            });
    }

    @ParameterizedTest
    @CsvSource({
        "address,??????????????? ????????? ?????????,0XFFFF,808,400,????????????,?????????,010,9534,4150,127.1313131,37.46961667,13123"})
    @DisplayName("poi create api ?????????")
    void testPoiCreateApi(String name,
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
        int zipCode) throws Exception {
        CreatePoi.Request request = new CreatePoi.Request(poiCode, address, primary, secondary, -1
            , fname, cname, phoneA, phoneB, phoneC, zipCode, lon, lan, "-1");
        log.info("request {}", request.toString());
        MvcResult result = mockMvc.perform(post("/api/poi")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("poi").exists())
            .andExpect(jsonPath("poi.poi_id").exists())
            .andExpect(jsonPath("poi.poi_code").exists())
            .andReturn();
        String poiId
            = JsonPath.read(result.getResponse().getContentAsString(), "poi.poi_id");

        mustDeletePoiId.add(poiId);
    }

    @ParameterizedTest
    @CsvSource({
        "address,??????????????? ????????? ?????????,0XFFFF,808,400,????????????,?????????,010,9534,4150,127.1313131,37.46961667,13123,????????????"})
    @DisplayName("poi update api ?????????")
    void testPoiCreateApi(String name,
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
        String changeFname) throws Exception {

        CreatePoi.Request request = new CreatePoi.Request(poiCode, address, primary, secondary, -1
            , fname, cname, phoneA, phoneB, phoneC, zipCode, lon, lan, "-1");

        Poi poi = poiService.createPoi(request);

        UpdatePoi.Request updateRequest = new UpdatePoi.Request(poiCode, changeFname, cname,
            phoneA, phoneB, phoneC, zipCode, lon, lan);
        MvcResult result = mockMvc.perform(put("/api/poi/")
                .param("id", poi.getPoiId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest))
            )
            .andDo(print())
            .andExpect(status().isCreated())
            .andReturn();

        String resultId = JsonPath.read(result.getResponse().getContentAsString(), "poi.poi_id");
        mustDeletePoiId.add(poi.getPoiId());
        Assertions.assertThat(resultId).isEqualTo(poi.getPoiId());
    }
}