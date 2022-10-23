package com.example.searchapi.poi.repository;

import com.example.searchapi.poi.model.Poi;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PoiQueryRepository {

    List<Poi> searchAddressByPrefixAddress(String address, PageRequest pageRequest);

    List<Poi> searchAddressByPoiCode(String poiCode, PageRequest pageRequest);

    List<Poi> searchAddressByPoiCodes(List<String> poiCategories, PageRequest pageRequest);

    List<Poi> searchPoiByName(String name, PageRequest pageRequest);

    List<Poi> searchPoiByNameFilterPoiCodes(String name, String field, String category,
        PageRequest of);
}
