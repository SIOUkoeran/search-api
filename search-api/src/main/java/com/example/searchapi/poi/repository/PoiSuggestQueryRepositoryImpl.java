package com.example.searchapi.poi.repository;

import com.example.searchapi.common.query.QuerySuggestUtils;
import com.example.searchapi.poi.model.Poi;
import java.util.List;
import java.util.Objects;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.suggest.response.Suggest;
import org.springframework.stereotype.Component;

@Component
public class PoiSuggestQueryRepositoryImpl implements PoiSuggestQueryRepository {

    private final QuerySuggestUtils querySuggestUtils;
    private final ElasticsearchOperations operations;

    public PoiSuggestQueryRepositoryImpl(QuerySuggestUtils querySuggestUtils,
        ElasticsearchOperations operations) {
        this.querySuggestUtils = querySuggestUtils;
        this.operations = operations;
    }

    @Override
    public Suggest.Suggestion<? extends Suggest.Suggestion.Entry<? extends Suggest.Suggestion.Entry.Option>> suggestPoiName(
        String input) {
        CompletionSuggestionBuilder completionQuery
            = querySuggestUtils.createCompletionQuery(input, 5, "poi_suggest");
        SuggestBuilder suggestQuery
            = querySuggestUtils.createSuggestBuilder(List.of("poi-suggest"), completionQuery);

        NativeSearchQuery query = new NativeSearchQueryBuilder()
            .withSuggestBuilder(suggestQuery)
            .build();
        
        SearchHits<Poi> poi = operations.search(query, Poi.class, IndexCoordinates.of("poi"));
        return Objects.requireNonNull(poi.getSuggest()).getSuggestion("poi-suggest");
    }
}
