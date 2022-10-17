package com.example.searchapi.category.service;

import com.example.searchapi.category.model.Category;
import com.example.searchapi.category.repository.CategoryQueryRepository;
import com.example.searchapi.category.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryQueryRepository categoryQueryRepository;

    public CategoryService(CategoryRepository categoryRepository, CategoryQueryRepository categoryQueryRepository) {
        this.categoryRepository = categoryRepository;
        this.categoryQueryRepository = categoryQueryRepository;
    }

    @Transactional(readOnly = true)
    public Category searchPoiByPoiCode(String poiCode){
        return this.categoryQueryRepository.searchPoiByPoiCode(poiCode);
    }

    @Transactional(readOnly = true)
    public List<String> searchPoiCodesByCategory(String category, String field) {
        return getPoiCodes(this.categoryQueryRepository
                .searchPoiByCategory(category, field));
    }

    private List<String> getPoiCodes(List<Category> pois){
        return pois.stream()
                .map(Category::getPoi_code)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<String> searchPoiCodesByCategoryField(String category, String field) {
        return getPoiCodes(this.categoryQueryRepository.searchCategoryCodeByField(category, field));
    }
}
