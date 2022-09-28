package com.example.searchapi.category.repository;

import com.example.searchapi.category.model.Category;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryQueryRepository {
    Category searchPoiByPoiCode(String poiCode);
    List<Category> searchPoiByLargeCategory(String category);
    List<Category> searchPoiByCategory(String category, String field);
    List<Category> searchPoiBySmallCategory(String smallCategory);

}
