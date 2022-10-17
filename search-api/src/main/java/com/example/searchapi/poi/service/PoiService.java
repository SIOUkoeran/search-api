package com.example.searchapi.poi.service;

import com.example.searchapi.category.exception.NotFoundPoiException;
import com.example.searchapi.poi.dto.CreatePoi;
import com.example.searchapi.poi.dto.UpdatePoi;
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
@Transactional
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

    public void deletePoi(String poiId) {
        log.info("delete poi : {} ", poiId);
        this.poiRepository.deleteById(poiId);
    }

    private String[] splitRequest(String fname, String cname) {
        return new String[]{
                fname + " " + cname,
                cname
        };
    }

    public UpdatePoi.Response updatePoi(String id, UpdatePoi.Request request) {
        Poi poi = this.poiRepository.findById(id)
                .orElseThrow(NotFoundPoiException::new);
        String[] input = splitRequest(request.getFname(), request.getCname());
        Poi update = poi.update(request, input);
        Poi save = this.poiRepository.save(update);
        return new UpdatePoi.Response(save);
    }

    @Transactional(readOnly = true)
    public List<Poi> searchPoiByNameFilterPoiCodes(String name, List<String> poiCodes, PageRequest of) {
        return this.poiQueryRepository.searchPoiByNameFilterPoiCodes(name, poiCodes, of);
    }
}
