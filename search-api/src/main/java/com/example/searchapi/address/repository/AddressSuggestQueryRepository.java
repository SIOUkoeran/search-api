package com.example.searchapi.address.repository;

import com.example.searchapi.address.model.Address;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.suggest.response.Suggest;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressSuggestQueryRepository {

    Suggest.Suggestion<? extends Suggest.Suggestion.Entry<? extends Suggest.Suggestion.Entry.Option>> suggestAddressByAddress(
        String address);

    List<SearchHit<Address>> suggestPrimaryBunByPrimary(String primaryBun);

    List<SearchHit<Address>> suggestSecondaryBunBySecondary(String secondaryBun);
}
