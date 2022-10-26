package com.example.searchapi.address.repository;

import static com.example.searchapi.address.model.AddressColumn.ADDRESS;
import static com.example.searchapi.address.model.AddressColumn.PRIMARYBUN;
import static com.example.searchapi.address.model.AddressColumn.SECONDATYBUN;
import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchQuery;
import static org.elasticsearch.index.query.QueryBuilders.multiMatchQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;
import static org.elasticsearch.index.query.QueryBuilders.termsQuery;

import com.example.searchapi.address.exception.NotFoundAddressException;
import com.example.searchapi.address.model.Address;
import com.example.searchapi.common.query.QueryUtils;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.search.sort.SortBuilders;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class AddressQueryRepositoryImpl implements AddressQueryRepository {

    private final ElasticsearchOperations operations;

    public AddressQueryRepositoryImpl(ElasticsearchOperations operations) {
        this.operations = operations;
    }

    @Override
    public List<Address> findAddressesByAddress(String address, String reversedAddressArray,
        String[] bun, PageRequest pageRequest) {

        NativeSearchQuery query = new NativeSearchQueryBuilder()
            .withMinScore(2.5f)
            .withQuery(boolQuery()
                .should(
                    multiMatchQuery(address)
                        .field(ADDRESS.getColumn())
                        .field("address._gram")
                        .fuzziness(Fuzziness.AUTO)
                )
                .should(
                    multiMatchQuery(bun[0], PRIMARYBUN.getColumn())
                        .boost(1.5f)
                        .fuzziness(Fuzziness.ONE)
                )
                .should(
                    multiMatchQuery(bun[1], SECONDATYBUN.getColumn())
                        .boost(1.2f)
                        .fuzziness(Fuzziness.ONE)
                )
            )
            .withPageable(pageRequest)
            .build();
        return QueryUtils.operationQueryReturnList(query, Address.class, "address", operations);
    }

    @Override
    public List<Address> findAddressesByBun(String primary_bun, String secondary_bun,
        PageRequest pageRequest) {
        NativeSearchQuery query = new NativeSearchQueryBuilder()
            .withQuery(boolQuery()
                .must(matchQuery("primary_bun", primary_bun))
                .must(matchQuery("secondary_bun", secondary_bun))
            ).withPageable(pageRequest)
            .build();
        return QueryUtils.operationQueryReturnList(query, Address.class, "address", operations);
    }

    @Override
    public Address findAddressByPoiId(String poiId) {
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
            .withQuery(termQuery("poi_id", poiId))
            .build();
        return operations.search(searchQuery, Address.class, IndexCoordinates.of("address"))
            .stream()
            .map(SearchHit::getContent)
            .findFirst()
            .orElseThrow(NotFoundAddressException::new);
    }

    @Override
    public List<Address> findAll() {
        NativeSearchQuery query = new NativeSearchQueryBuilder()
            .withQuery(matchAllQuery())
            .build();
        return operations.search(query, Address.class, IndexCoordinates.of("address"))
            .stream()
            .map(SearchHit::getContent)
            .collect(Collectors.toList());
    }

    @Override
    public List<Address> findFirstQueryAllAddressByPoiCodes(List<String> poiCodes) {
        NativeSearchQuery query = new NativeSearchQueryBuilder()
            .withQuery(termsQuery("poi_code", poiCodes))
            .withSorts(SortBuilders.scoreSort())
            .withMaxResults(1000)
            .withSorts(SortBuilders.fieldSort("poi_id"))
            .build();
        return QueryUtils.operationQueryReturnList(query, Address.class, "address", operations);
    }

    @Override
    public List<Address> findNextQueryAllAddressByPoiCodes(List<String> poiCodes,
        List<Object> searchAfter) {
        NativeSearchQuery query = new NativeSearchQueryBuilder()
            .withQuery(termsQuery("poi_code", poiCodes))
            .withSorts(SortBuilders.scoreSort())
            .withMaxResults(1000)
            .withSorts(SortBuilders.fieldSort("poi_id"))
            .build();
        return QueryUtils.searchAfter(Address.class, query, "address", searchAfter, operations);
    }
}
