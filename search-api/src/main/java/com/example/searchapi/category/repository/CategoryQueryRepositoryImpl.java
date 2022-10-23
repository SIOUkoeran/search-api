package com.example.searchapi.category.repository;

import com.example.searchapi.category.exception.NotFoundPoiException;
import com.example.searchapi.category.model.Category;
import com.example.searchapi.common.query.QueryUtils;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.QueryBuilders.matchQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;

@Component
public class CategoryQueryRepositoryImpl implements CategoryQueryRepository {

    private final ElasticsearchOperations operations;

    public CategoryQueryRepositoryImpl(ElasticsearchOperations operations) {
        this.operations = operations;
    }

    public Category searchPoiByPoiCode(String poiCode) {
        NativeSearchQuery searchPoiQuery = new NativeSearchQueryBuilder()
            .withQuery(matchQuery("poi_code", poiCode))
            .build();
        return operations.search(searchPoiQuery, Category.class, IndexCoordinates.of("poi"))
            .stream()
            .map(SearchHit::getContent)
            .findFirst()
            .orElseThrow(NotFoundPoiException::new);
    }

    @Override
    public List<Category> searchPoiByCategory(String category, String field) {
        NativeSearchQuery query = new NativeSearchQueryBuilder()
            .withQuery(termQuery(field, category))
            .withSorts(SortBuilders.scoreSort().order(SortOrder.DESC))
            .build();
        return QueryUtils.operationQueryReturnList(query, Category.class, "category", operations);
    }

    @Override
    public List<Category> searchPoiBySmallCategory(String smallCategory) {
        NativeSearchQuery query = new NativeSearchQueryBuilder()
            .withQuery(termQuery("small_category", smallCategory))
            .withSorts(SortBuilders.scoreSort())
            .build();
        return QueryUtils.operationQueryReturnList(query, Category.class, "category", operations);
    }

    @Override
    public List<Category> searchPoiByLargeCategory(String category) {
        NativeSearchQuery searchPoiQuery = new NativeSearchQueryBuilder()
            .withQuery(termQuery("large_category", category))
            .build();
        return QueryUtils.operationQueryReturnList(searchPoiQuery, Category.class, "poi",
            operations);
    }

    @Override
    public List<Category> searchCategoryCodeByField(String category, String field) {
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
            .withQuery(termQuery(field, category))
            .build();
        return QueryUtils.operationQueryReturnList(searchQuery, Category.class, "category",
            operations);
    }
}
