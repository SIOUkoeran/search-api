package com.example.searchapi.poi.repository;

import com.example.searchapi.poi.model.Poi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.indices.AnalyzeRequest;
import org.elasticsearch.client.indices.AnalyzeResponse;
import org.elasticsearch.common.unit.Fuzziness;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.MultiMatchQueryBuilder.Type.BOOL_PREFIX;
import static org.elasticsearch.index.query.QueryBuilders.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class PoiQueryRepositoryImpl implements PoiQueryRepository {

    private final ElasticsearchOperations operations;

    @Override
    public List<Poi> searchAddressByPrefixAddress(String address, PageRequest pageRequest){
        NativeSearchQuery findQueryByAddress = new NativeSearchQueryBuilder()
                .withQuery(matchQuery("address", address))
                .withPageable(pageRequest)
                .build();

        return operations.search(findQueryByAddress, Poi.class, IndexCoordinates.of("address"))
                .stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }

    public List<Poi> searchAddressByPoiCode(String poiCode, PageRequest pageRequest){
        NativeSearchQuery findQueryByPoiCode = new NativeSearchQueryBuilder()
                .withQuery(matchQuery("poi_code", poiCode))
                .withPageable(pageRequest)
                .build();
        return operations.search(findQueryByPoiCode, Poi.class, IndexCoordinates.of("address"))
                .stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }

    @Override
    public List<Poi> searchAddressByPoiCodes(List<String> poiCodes, PageRequest pageRequest) {

        NativeSearchQuery big_categoryQuery = new NativeSearchQueryBuilder()
                .withQuery(termsQuery("poi_code", poiCodes))
                .withPageable(pageRequest)
                .build();
        return operations.search(big_categoryQuery, Poi.class, IndexCoordinates.of("address"))
                .stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }

    @Override
    public List<Poi> searchPoiByName(String name, PageRequest pageRequest) {

        NativeSearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(boolQuery()
                        .should(multiMatchQuery(name)
                                .field("cname")
                                .field("fname", 1.5f)
                                .field("fname.edge")
                                        .fuzziness(Fuzziness.TWO)
                                )
                        )
                .withPageable(pageRequest)
                .build();
        return operations.search(query, Poi.class, IndexCoordinates.of("poi"))
                .stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }
    @Override
    public List<Poi> searchPoiByNameFilterPoiCodes(String name, List<String> poiCode, PageRequest pageRequest) {
        NativeSearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(boolQuery()
                        .must(multiMatchQuery(name)
                                .field("cname")
                                .field("fname", 1.5f)
                                .field("fname.edge")
                                .fuzziness(Fuzziness.TWO)
                        )
                        .filter(
                                termsQuery("poi_code", poiCode)
                        )
                )
                .withPageable(pageRequest)
                .build();
        return operations.search(query, Poi.class, IndexCoordinates.of("poi"))
                .stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }

    private String splitQueryReturnReverse(String query) {
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
        log.info("sb output {}", sb);
        return String.valueOf(sb);
    }

    private String reverseInput(String input) {
        StringBuilder sb = new StringBuilder();
        for (int i = input.length() - 1; i >= 0; i--){
            sb.append(input.charAt(i));
        }
        return String.valueOf(sb);
    }
}
