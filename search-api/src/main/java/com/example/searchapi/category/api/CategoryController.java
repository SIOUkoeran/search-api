package com.example.searchapi.category.api;

import com.example.searchapi.category.model.Category;
import com.example.searchapi.category.service.CategoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/{poi}")
    public Category findByPoi(@RequestParam("poi") String poi){
        return this.categoryService.searchPoiByPoiCode(poi);
    }
}
