package com.example.searchapi.poi.service;

import com.example.searchapi.poi.dto.CreatePoi;
import com.example.searchapi.poi.model.Poi;
import com.example.searchapi.poi.repository.PoiQueryRepository;
import com.example.searchapi.poi.repository.PoiRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class PoiService {

    private final PoiQueryRepository poiQueryRepository;
    private final PoiRepository poiRepository;

    public PoiService(PoiQueryRepository poiQueryRepository, PoiRepository poiRepository) {
        this.poiQueryRepository = poiQueryRepository;
        this.poiRepository = poiRepository;
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

    public Poi createPoi(CreatePoi.Request request) {
        String[] input = splitRequest(request.getFname(), request.getCname());
        return this.poiRepository.save(new Poi(request, input));
    }


    private String[] splitRequest(String fname, String cname) {
        return new String[]{
                fname + " " + cname,
                cname
        };
    }
}
