package com.example.searchapi.address.api;


import com.example.searchapi.BaseTest;
import org.apache.http.HttpHeaders;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;
import java.util.Map;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@SpringBootTest
@AutoConfigureMockMvc
public class AddressControllerTest extends BaseTest {

    @Autowired
    MockMvc mockMvc;


    @Test
    void findAllAddressByCategory() throws Exception{
        String category = "쇼핑";
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("large_category", category);
        params.add("page", "0");
        mockMvc.perform(get("/api/address").params(params)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("content").exists())
                .andExpect(jsonPath("content", Matchers.hasSize(6)))
                ;
    }

    
}
