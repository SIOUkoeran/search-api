package com.example.searchapi.address.repository;

import com.example.searchapi.BaseTest;
import com.example.searchapi.address.model.Address;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.suggest.response.CompletionSuggestion;
import org.springframework.data.elasticsearch.core.suggest.response.Suggest;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.data.elasticsearch.core.suggest.response.CompletionSuggestion.*;

@SpringBootTest
class AddressSuggestQueryRepositoryImplTest extends BaseTest {

    @Autowired
    AddressSuggestQueryRepository addressSuggestQueryRepository;

    private final Logger logger = LoggerFactory.getLogger(AddressSuggestQueryRepositoryImplTest.class);

    @Test
    void testSuggestAddressQuery() {
        String input = "중랑";
        CompletionSuggestion compSuggestion =
                (CompletionSuggestion) this.addressSuggestQueryRepository.suggestAddressByAddress(input);

        ObjectMapper objectMapper = new ObjectMapper();

        List<CompletionSuggestion.Entry> entryList = compSuggestion.getEntries();
        if(entryList != null) {
            for (CompletionSuggestion.Entry entry : entryList) {
                List<CompletionSuggestion.Entry.Option> options = entry.getOptions();
                if (options != null) {
                    for (CompletionSuggestion.Entry.Option option : options) {
                        SearchHit<Address> searchHit = option.getSearchHit();
                        String address = searchHit.getContent().getAddress();
                        logger.info("address {}" , address);
                    }
                }
            }
        }

    }
}