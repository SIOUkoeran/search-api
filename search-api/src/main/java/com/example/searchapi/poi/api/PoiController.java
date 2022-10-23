package com.example.searchapi.poi.api;

import com.example.searchapi.address.dto.CreateAddressDto;
import com.example.searchapi.address.service.AddressService;
import com.example.searchapi.category.service.CategoryService;
import com.example.searchapi.poi.dto.CreatePoi;
import com.example.searchapi.poi.dto.UpdatePoi;
import com.example.searchapi.poi.model.Poi;
import com.example.searchapi.poi.service.PoiService;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/poi")
public class PoiController {

    private final PoiService poiService;
    private final CategoryService categoryService;
    private final AddressService addressService;

    public PoiController(PoiService poiService, CategoryService categoryService,
        AddressService addressService) {
        this.poiService = poiService;
        this.categoryService = categoryService;
        this.addressService = addressService;
    }

    @GetMapping(value = "", params = {"address", "page"})
    public List<Poi> searchAllPoiByAddress(@RequestParam("address") String address,
        @RequestParam("page") int page) {
        List<String> poi_ids = this.addressService.searchPoiIdByAddress(address,
            PageRequest.of(page, 20));
        return this.poiService.searchQueryByPoiCodes(poi_ids, PageRequest.of(page, 20));
    }

    @GetMapping(value = "", params = {"name"})
    public List<Poi> searchAllPoiByName(@RequestParam("name") String name,
        @RequestParam("page") int page) {
        return this.poiService.searchQueryByName(name, PageRequest.of(page, 20));
    }

    @PostMapping(value = "")
    public ResponseEntity<CreatePoi.Response> createPoi(
        @Valid @RequestBody CreatePoi.Request request) {
        Poi poi = this.poiService.createPoi(request);
        CreatePoi.Response response = new CreatePoi.Response(poi,
            this.addressService.createAddress(new CreateAddressDto.Request(request),
                poi.getPoi_id()));
        return ResponseEntity.status(201).body(response);
    }

    @DeleteMapping(value = "", params = {"id"})
    public ResponseEntity<String> deletePoi(@RequestParam String id) {
        this.poiService.deletePoi(id);
        this.addressService.deleteAddress(id);
        return ResponseEntity.status(204).body("DELETE SUCCESS");
    }

    @PutMapping(value = "", params = {"id"})
    public ResponseEntity<UpdatePoi.Response> updatePoi(@RequestParam("id") String id,
        @Valid @RequestBody UpdatePoi.Request request) {
        UpdatePoi.Response response = this.poiService.updatePoi(id, request);

        return ResponseEntity.status(201).body(response);
    }
}
