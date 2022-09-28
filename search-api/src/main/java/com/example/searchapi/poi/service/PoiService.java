package com.example.searchapi.poi.service;

import com.example.searchapi.poi.model.Poi;
import com.example.searchapi.poi.repository.PoiQueryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class PoiService {

    private final PoiQueryRepository poiQueryRepository;

    public PoiService(PoiQueryRepository poiQueryRepository) {
        this.poiQueryRepository = poiQueryRepository;
    }

    @Transactional(readOnly = true)
    public List<Poi> searchQueryByAddress(String address, PageRequest pageRequest){
        log.info("request address {}", address);
        return poiQueryRepository.searchAddressByPrefixAddress(address, pageRequest);

    }

    @Transactional(readOnly = true)
    public List<Poi> searchQueryByPoiCode(String poiCode , PageRequest pageRequest) {
        log.info("request address by poi_code {}", poiCode);
        return poiQueryRepository.searchAddressByPoiCode(poiCode, pageRequest);
    }

    @Transactional(readOnly = true)
    public List<Poi> searchQueryByPoiCodes(List<String> poiCode , PageRequest pageRequest) {
        log.info("request address by poi_code {}", poiCode);
        return poiQueryRepository.searchAddressByPoiCodes(poiCode, pageRequest);
    }

    @Transactional(readOnly = true)
    public List<Poi> searchQueryByCategory(List<String> poiCategories, PageRequest pageRequest) {
        log.info("request address by Category");
        return this.poiQueryRepository.searchAddressByPoiCodes(poiCategories, pageRequest);
    }

    public List<Poi> searchQueryByName(String name, PageRequest pageRequest) {
        log.info("request poi by name");
        return this.poiQueryRepository.searchPoiByName(name, pageRequest);
    }
}
