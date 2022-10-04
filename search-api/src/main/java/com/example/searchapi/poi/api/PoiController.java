package com.example.searchapi.poi.api;

import com.example.searchapi.address.service.AddressService;
import com.example.searchapi.category.service.CategoryService;
import com.example.searchapi.poi.model.Poi;
import com.example.searchapi.poi.service.PoiService;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/poi")
public class PoiController {

    private final PoiService poiService;
    private final CategoryService categoryService;
    private final AddressService addressService;

    public PoiController(PoiService poiService, CategoryService categoryService, AddressService addressService) {
        this.poiService = poiService;
        this.categoryService = categoryService;
        this.addressService = addressService;
    }

    @GetMapping(value ="", params = {"address", "page"})
    public List<Poi> searchAllPoiByAddress(@RequestParam("address") String address,
                                           @RequestParam("page") int page){
        List<String> poi_ids = this.addressService.searchPoiIdByAddress(address, PageRequest.of(page, 20));
        return this.poiService.searchQueryByPoiCodes(poi_ids, PageRequest.of(page, 20));
    }

    @GetMapping(value ="", params = {"name"})
    public List<Poi> searchAllPoiByName(@RequestParam("name") String name,
                                        @RequestParam("page") int page) {
        return this.poiService.searchQueryByName(name, PageRequest.of(page, 20));
    }

}
