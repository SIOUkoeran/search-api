package com.example.searchapi.address.api;

import com.example.searchapi.address.dto.SuggestAddressDto;
import com.example.searchapi.address.service.AddressService;
import com.example.searchapi.address.service.AddressSuggestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/address/suggest")
public class AddressSuggestController {

    private final AddressService addressService;
    private final AddressSuggestService addressSuggestService;

    public AddressSuggestController(AddressService addressService, AddressSuggestService addressSuggestService) {
        this.addressService = addressService;
        this.addressSuggestService = addressSuggestService;
    }

    @GetMapping(value = "")
    public ResponseEntity<SuggestAddressDto.Response> suggestAddressByAddress(@RequestBody SuggestAddressDto.Request request) {
        return ResponseEntity.ok().body(this.addressSuggestService.suggestAddress(request));
    }
}
