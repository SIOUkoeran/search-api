package com.example.searchapi.address.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
public class RequestAddressSearchAfter {

    List<Object> searchAfter;
    List<String> poiCodes;

    public RequestAddressSearchAfter(List<Object> searchAfter, List<String> poiCodes) {
        this.searchAfter = searchAfter;
        this.poiCodes = poiCodes;
    }
}
