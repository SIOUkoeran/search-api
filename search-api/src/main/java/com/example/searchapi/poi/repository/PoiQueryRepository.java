package com.example.searchapi.poi.repository;

import com.example.searchapi.poi.model.Poi;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

@Repository
public interface PoiQueryRepository {

    List<Poi> searchAddressByPrefixAddress(String address, PageRequest pageRequest);

    List<Poi> searchAddressByPoiCode(String poiCode, PageRequest pageRequest);

    List<Poi> searchAddressByPoiCodes(List<String> poiCategories, PageRequest pageRequest);

    List<Poi> searchPoiByName(String name, PageRequest pageRequest, float minScore);

    List<Poi> searchPoiByNameFilterPoiCodes(String name, String field, String category,
        PageRequest of, float minScore);
}
