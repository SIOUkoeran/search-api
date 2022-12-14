package com.example.searchapi.address.api;

import com.example.searchapi.address.dto.RequestAddressSearchAfter;
import com.example.searchapi.address.dto.UpdateAddress;
import com.example.searchapi.address.model.Address;
import com.example.searchapi.address.service.AddressService;
import com.example.searchapi.category.service.CategoryService;
import com.example.searchapi.poi.service.PoiService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/address")
@Slf4j
public class AddressController {

    private final AddressService addressService;
    private final PoiService poiService;
    private final CategoryService categoryService;

    public AddressController(AddressService addressService, PoiService poiService,
        CategoryService categoryService) {
        this.addressService = addressService;
        this.poiService = poiService;
        this.categoryService = categoryService;
    }

    @GetMapping(value = "", params = {"poi_id"})
    public Address searchAddressByPoiId(@RequestParam("poi_id") String poiId) {
        return this.addressService.searchAddressByPoiId(poiId);
    }

    @GetMapping(value = "", params = {"address"})
    public List<Address> searchAllAddressByAddress(@RequestParam("address") String address,
        @RequestParam("page") int page) {
        return this.addressService.searchAllAddressByAddress(address, PageRequest.of(page, 20));
    }

    @GetMapping(value = "", params = {"large_category"})
    public Page<Address> searchAllAddressByBigCategory(
        @RequestParam("large_category") String category,
        @RequestParam("page") int page) {
        List<String> poiCodes = this.categoryService.searchPoiCodesByCategory(category,
            "large_category");
        log.info("poiCodes size : {}", poiCodes.size());
        return this.addressService.searchFirstAllAddressByPoiCodes(poiCodes, page, 20);
    }

    @GetMapping(value = "/next", params = {"large_category"})
    public Page<Address> searchAllAddressByBigCategoryNextPage(
        @RequestParam("large_category") String category,
        @RequestParam("page") int page,
        @RequestBody RequestAddressSearchAfter request) {
        List<String> poiCodes = this.categoryService.searchPoiCodesByCategory(category,
            "large_category");
        return this.addressService.searchNextAllAddressByPoiCodes(poiCodes, page, 20,
            List.of(request));
    }

    @GetMapping(value = "", params = {"medium_category"})
    public Page<Address> searchAllAddressesByMediumCategory(
        @RequestParam("medium_category") String category,
        @RequestParam("page") int page) {
        List<String> poiCodes = this.categoryService.searchPoiCodesByCategory(category,
            "medium_category");
        return this.addressService.searchFirstAllAddressByPoiCodes(poiCodes, page, 20);
    }

    @GetMapping(value = "/next", params = "medium_category")
    public Page<Address> searchAllAddressesByMediumCategoryNextPage(
        @RequestParam("medium_category") String category,
        @RequestParam("page") int page,
        @RequestBody RequestAddressSearchAfter request) {
        List<String> poiCodes = this.categoryService.searchPoiCodesByCategory(category,
            "medium_category");
        return this.addressService.searchNextAllAddressByPoiCodes(poiCodes, page, 20,
            List.of(request));
    }

    @GetMapping(value = "", params = {"small_category"})
    public Page<Address> searchAllAddressesBySmallCategory(
        @RequestParam("medium_category") String category,
        @RequestParam("page") int page) {
        List<String> poiCodes = this.categoryService.searchPoiCodesByCategory(category,
            "medium_category");
        return this.addressService.searchFirstAllAddressByPoiCodes(poiCodes, page, 20);
    }

    @GetMapping(value = "/next", params = "small_category")
    public Page<Address> searchAllAddressesBySmallCategoryNextPage(
        @RequestParam("small_category") String category,
        @RequestParam("page") int page,
        @RequestBody RequestAddressSearchAfter request) {
        List<String> poiCodes = this.categoryService.searchPoiCodesByCategory(category,
            "small_category");
        return this.addressService.searchNextAllAddressByPoiCodes(poiCodes, page, 20,
            List.of(request));
    }

    @PutMapping(value = "", params = {"id"})
    public ResponseEntity<UpdateAddress.Response> updateAddress(@RequestParam("id") String id,
        @RequestBody UpdateAddress.Request request) {
        return ResponseEntity
            .status(204)
            .body(this.addressService.updateAddress(id, request));
    }
}
