package com.example.searchapi.poi.repository;

import org.springframework.data.elasticsearch.core.suggest.response.Suggest;
import org.springframework.stereotype.Repository;

@Repository
public interface PoiSuggestQueryRepository {


    Suggest.Suggestion<? extends Suggest.Suggestion.Entry<? extends Suggest.Suggestion.Entry.Option>> suggestPoiName(
        String input);
}
