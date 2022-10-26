package com.example.searchapi.poi.api;


import com.example.searchapi.poi.dto.PoiSuggestDto;
import com.example.searchapi.poi.service.PoiService;
import com.example.searchapi.poi.service.PoiSuggestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/poi/suggest")
@Slf4j
public class PoiSuggestController {

    private final PoiService poiService;
    private final PoiSuggestService poiSuggestService;

    public PoiSuggestController(PoiService poiService, PoiSuggestService poiSuggestService) {
        this.poiService = poiService;
        this.poiSuggestService = poiSuggestService;
    }

    @GetMapping(value = "")
    public ResponseEntity<PoiSuggestDto.Response> suggestPoiName(
        @RequestBody PoiSuggestDto.Request request) {
        log.info("request suggest {}", request.getPoi());
        return ResponseEntity.ok(this.poiSuggestService.suggestPoi(request));
    }
}
