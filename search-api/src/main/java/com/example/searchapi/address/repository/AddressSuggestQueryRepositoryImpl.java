package com.example.searchapi.address.repository;

import com.example.searchapi.address.model.Address;
import com.example.searchapi.common.query.QuerySuggestUtils;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.suggest.response.Suggest;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
@Slf4j
public class AddressSuggestQueryRepositoryImpl implements AddressSuggestQueryRepository {

    private final ElasticsearchOperations operations;
    private final QuerySuggestUtils querySuggestUtils;

    public AddressSuggestQueryRepositoryImpl(ElasticsearchOperations operations,
        QuerySuggestUtils querySuggestUtils) {
        this.operations = operations;
        this.querySuggestUtils = querySuggestUtils;
    }

    @Override
    public Suggest.Suggestion<? extends Suggest.Suggestion.Entry<? extends Suggest.Suggestion.Entry.Option>> suggestAddressByAddress(
        String address) {
        CompletionSuggestionBuilder completionQuery = querySuggestUtils.createCompletionQuery(
            address, 5, "address_suggest.suggest");
        SuggestBuilder suggestQuery = querySuggestUtils.createSuggestBuilder(
            List.of("address-suggest"), completionQuery);
        NativeSearchQuery query = new NativeSearchQueryBuilder()
            .withSuggestBuilder(suggestQuery)
            .build();
        log.debug("request suggest query {}", query.toString());
        SearchHits<Address> responseAddress = operations.search(query, Address.class,
            IndexCoordinates.of("address"));
        log.debug("response suggest address {}", responseAddress.getSuggest());
        return responseAddress.getSuggest().getSuggestion("address-suggest");
    }

    @Override
    public List<SearchHit<Address>> suggestPrimaryBunByPrimary(String primaryBun) {
        CompletionSuggestionBuilder completionQuery = querySuggestUtils.createCompletionQuery(
            primaryBun, 5, "address_suggest");
        SuggestBuilder suggestQuery = querySuggestUtils.createSuggestBuilder(
            List.of("primary-suggest"), completionQuery);
        NativeSearchQuery query = new NativeSearchQueryBuilder()
            .withSuggestBuilder(suggestQuery)
            .build();
        return operations.search(query, Address.class, IndexCoordinates.of("address"))
            .getSearchHits();
    }

    @Override
    public List<SearchHit<Address>> suggestSecondaryBunBySecondary(String secondaryBun) {

        return new ArrayList<SearchHit<Address>>();
    }

}
