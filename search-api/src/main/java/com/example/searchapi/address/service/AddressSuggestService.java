package com.example.searchapi.address.service;

import com.example.searchapi.address.dto.SuggestAddressDto;
import com.example.searchapi.address.repository.AddressSuggestQueryRepository;
import com.example.searchapi.common.query.QuerySuggestUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.core.suggest.response.CompletionSuggestion;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class AddressSuggestService {

    private final AddressSuggestQueryRepository addressSuggestQueryRepository;
    private final QuerySuggestUtils querySuggestUtils;

    public AddressSuggestService(AddressSuggestQueryRepository addressSuggestQueryRepository, QuerySuggestUtils querySuggestUtils) {
        this.addressSuggestQueryRepository = addressSuggestQueryRepository;
        this.querySuggestUtils = querySuggestUtils;
    }

    @Transactional(readOnly = true)
    public SuggestAddressDto.Response suggestAddress(SuggestAddressDto.Request suggest){
        CompletionSuggestion compSuggestion =
                (CompletionSuggestion) this.addressSuggestQueryRepository.suggestAddressByAddress(suggest.getAddress());
        List<String> addressList = querySuggestUtils.convertToAddressList(compSuggestion);
        return new SuggestAddressDto.Response(addressList);
    }


}
