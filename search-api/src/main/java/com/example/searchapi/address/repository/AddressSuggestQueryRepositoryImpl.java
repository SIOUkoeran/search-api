package com.example.searchapi.address.repository;

import com.example.searchapi.address.model.Address;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.SuggestionBuilder;
import org.elasticsearch.search.suggest.term.TermSuggestionBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.suggest.response.Suggest;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@Slf4j
public class AddressSuggestQueryRepositoryImpl implements AddressSuggestQueryRepository{

    private final ElasticsearchOperations operations;

    public AddressSuggestQueryRepositoryImpl(ElasticsearchOperations operations) {
        this.operations = operations;
    }

    @Override
    public List<String> suggestAddressByAddress(String address) {
        TermSuggestionBuilder suggestTermQuery = new TermSuggestionBuilder("address").text(address)
                .size(5);
        SuggestBuilder suggestQuery = new SuggestBuilder().addSuggestion("address_suggestion", suggestTermQuery);
        NativeSearchQuery query = new NativeSearchQueryBuilder()
                .withSuggestBuilder(suggestQuery)
                .build();
        Suggest suggestions = operations.search(query, Address.class, IndexCoordinates.of("address"))
                .getSuggest();
        return suggestions.getSuggestions().stream()
                .map(Suggest.Suggestion::getEntries)
                .flatMap(Collection::stream)
                .map(Suggest.Suggestion.Entry::getText)
                .collect(Collectors.toList());
    }
}
