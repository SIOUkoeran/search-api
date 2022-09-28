package com.example.searchapi.address.service;

import com.example.searchapi.address.dto.SuggestAddressDto;
import com.example.searchapi.address.repository.AddressSuggestQueryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class AddressSuggestService {

    private final AddressSuggestQueryRepository addressSuggestQueryRepository;

    public AddressSuggestService(AddressSuggestQueryRepository addressSuggestQueryRepository) {
        this.addressSuggestQueryRepository = addressSuggestQueryRepository;
    }

    @Transactional(readOnly = true)
    public SuggestAddressDto.Response suggestAddress(SuggestAddressDto.Request suggest){
        return new SuggestAddressDto.Response(
                addressSuggestQueryRepository
                        .suggestAddressByAddress(suggest.getAddress())
                );
    }
}
