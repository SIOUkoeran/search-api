package com.example.searchapi.common.query;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.indices.AnalyzeRequest;
import org.elasticsearch.client.indices.AnalyzeResponse;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
@Slf4j
public class QueryUtils {

    private final ElasticsearchOperations operations;

    public QueryUtils(ElasticsearchOperations operations) {
        this.operations = operations;
    }

    public static<T> List<T> operationQueryReturnList(NativeSearchQuery query, Class<T> t, String index,
                                                       ElasticsearchOperations operations) {
        return operations.search(query, t, IndexCoordinates.of(index))
                .stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }

    public static<T> T operationQuery(NativeSearchQuery query, Class<T> t, String index,
                                                      ElasticsearchOperations operations) {
        Constructor<T> constructor = null;
        try {
            constructor = t.getConstructor();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        try {
            return operations.search(query, t, IndexCoordinates.of(index))
                    .stream()
                    .map(SearchHit::getContent)
                    .findFirst()
                    .orElse(constructor.newInstance());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException();
        }
    }

    public String splitQueryReturnReverse(String query) {
        AnalyzeRequest standard = AnalyzeRequest.withGlobalAnalyzer("standard", query);
        ElasticsearchRestTemplate restTemplate = (ElasticsearchRestTemplate) this.operations;
        AnalyzeResponse execute = restTemplate.execute(client -> client.indices().analyze(standard, RequestOptions.DEFAULT));
        AtomicInteger atomicInteger = new AtomicInteger(0);
        StringBuilder sb = new StringBuilder();
        Collections.reverse(execute.getTokens());
        execute.getTokens()
                .stream()
                .map(AnalyzeResponse.AnalyzeToken::getTerm)
                .forEach(t -> {
                    if (atomicInteger.getAndIncrement() == 0){
                        sb.append(t).append(" ");
                    }else
                        sb.append(t).append(" ");
                });
        return String.valueOf(sb);
    }

    public String[] splitQueryReturnStringArray(String query) {
        AnalyzeRequest standard = AnalyzeRequest.withGlobalAnalyzer("standard", query);
        ElasticsearchRestTemplate restTemplate = (ElasticsearchRestTemplate) this.operations;
        AnalyzeResponse execute = restTemplate.execute(client -> client.indices().analyze(standard, RequestOptions.DEFAULT));

        return execute.getTokens()
                .stream()
                .map(AnalyzeResponse.AnalyzeToken::getTerm)
                .toArray(String[]::new);

    }

    /**
     *
     * @param address
     * @param c
     * @return
     */
    public String[] splitQueryUntilChar(String address, char c){
        int idx = address.indexOf('-');
        if (idx == -1){
            return new String[] {address, ""};
        }
        return new String[] {address.substring(0, idx), address.substring(idx)};
    }

    /**
     * search_after 통해 전체 조회
     * @param clazz
     * @param query
     * @param operations
     * @param index
     * @param <T>
     * @return
     */
    public static<T> List<T> searchAfter(Class<T> clazz, Query query, String index, List<Object> searchAfter, ElasticsearchOperations operations) {


        List<T> returnResult = new ArrayList<>();
        while (true){
            query.setSearchAfter(searchAfter);
            List<SearchHit<T>> queryResult =
                    operations.search(query, clazz, IndexCoordinates.of(index))
                    .stream()
                    .collect(Collectors.toList());
            if (queryResult.size() == 0)
                break;
            returnResult.addAll(
                    queryResult.stream()
                            .map(SearchHit::getContent)
                            .collect(Collectors.toList())
            );
            log.info("query result size {}", queryResult.size());
            log.info("query result last {}", queryResult.get(queryResult.size() - 1).getSortValues().toString());
            searchAfter = queryResult.get(queryResult.size() - 1).getSortValues();
            log.info("searchAfter {}", searchAfter.get(0).toString());
        }
        return returnResult;
    }
}
