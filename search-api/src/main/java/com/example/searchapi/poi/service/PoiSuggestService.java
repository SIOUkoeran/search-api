package com.example.searchapi.poi.service;

import com.example.searchapi.address.model.Address;
import com.example.searchapi.common.query.QuerySuggestUtils;
import com.example.searchapi.poi.dto.PoiSuggestDto;
import com.example.searchapi.poi.repository.PoiQueryRepository;
import com.example.searchapi.poi.repository.PoiSuggestQueryRepository;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.suggest.response.CompletionSuggestion;
import org.springframework.data.elasticsearch.core.suggest.response.Suggest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class PoiSuggestService {

    private PoiQueryRepository poiQueryRepository;
    private PoiSuggestQueryRepository poiSuggestQueryRepository;
    private QuerySuggestUtils querySuggestUtils;

    public PoiSuggestService(PoiQueryRepository poiQueryRepository,
        PoiSuggestQueryRepository poiSuggestQueryRepository, QuerySuggestUtils querySuggestUtils) {
        this.poiQueryRepository = poiQueryRepository;
        this.poiSuggestQueryRepository = poiSuggestQueryRepository;
        this.querySuggestUtils = querySuggestUtils;
    }

    @Transactional(readOnly = true)
    public PoiSuggestDto.Response suggestPoi(PoiSuggestDto.Request request) {

        CompletionSuggestion completionSuggestion
            = (CompletionSuggestion) this.poiSuggestQueryRepository.suggestPoiName(
            request.getPoi());
        List<String> strings = querySuggestUtils.convertToPoiNameList(completionSuggestion);
        return new PoiSuggestDto.Response(strings);
    }
}
