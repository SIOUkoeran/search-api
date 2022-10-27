package com.example.searchapi.poi.api;

import com.example.searchapi.category.service.CategoryService;
import com.example.searchapi.poi.dto.SearchPoi;
import com.example.searchapi.poi.service.PoiService;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2/poi")
public class PoiControllerV2 {

    private final PoiService poiService;
    private final CategoryService categoryService;

    public PoiControllerV2(PoiService poiService, CategoryService categoryService) {
        this.poiService = poiService;
        this.categoryService = categoryService;
    }

    @GetMapping(value = "", params = {"name", "category", "page", "field", "score"})
    public ResponseEntity<SearchPoi.Response> searchPoiWithFilter(@RequestParam("name") String name,
        @RequestParam(value = "category", required = false) String category,
        @RequestParam(value = "field", required = false) String field,
        @RequestParam("page") int page,
        @RequestParam("score") float score) {
        if (category.isEmpty() || field.isEmpty()) {
            return ResponseEntity.status(200)
                .body(new SearchPoi.Response(
                    this.poiService.searchQueryByName(name, PageRequest.of(page, 20), score)));
        }

        return ResponseEntity.status(200)
            .body(new SearchPoi.Response(
                this.poiService.searchPoiByNameFilterPoiCodes(name, field, category, score,
                    PageRequest.of(page, 20))));
    }
}