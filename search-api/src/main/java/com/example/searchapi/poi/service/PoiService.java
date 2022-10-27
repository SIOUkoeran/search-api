package com.example.searchapi.poi.service;

import com.example.searchapi.category.exception.NotFoundPoiException;
import com.example.searchapi.poi.dto.CreatePoi;
import com.example.searchapi.poi.dto.UpdatePoi;
import com.example.searchapi.poi.model.Poi;
import com.example.searchapi.poi.repository.PoiQueryRepository;
import com.example.searchapi.poi.repository.PoiRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public List<Poi> searchQueryByAddress(String address, PageRequest pageRequest) {
        log.info("request address {}", address);
        return poiQueryRepository.searchAddressByPrefixAddress(address, pageRequest);

    }

    @Transactional(readOnly = true)
    public Poi searchPoiByPoiId(String poiId) {
        return poiRepository.findPoiByPoiId(poiId)
            .orElseThrow(NotFoundPoiException::new);
    }

    @Transactional(readOnly = true)
    public List<Poi> searchQueryByPoiCode(String poiCode, PageRequest pageRequest) {
        log.info("request address by poi_code {}", poiCode);
        return poiQueryRepository.searchAddressByPoiCode(poiCode, pageRequest);
    }

    @Transactional(readOnly = true)
    public List<Poi> searchQueryByPoiCodes(List<String> poiCode, PageRequest pageRequest) {
        log.info("request address by poi_code {}", poiCode);
        return poiQueryRepository.searchAddressByPoiCodes(poiCode, pageRequest);
    }

    @Transactional(readOnly = true)
    public List<Poi> searchQueryByCategory(List<String> poiCategories, PageRequest pageRequest) {
        log.info("request address by Category");
        return this.poiQueryRepository.searchAddressByPoiCodes(poiCategories, pageRequest);
    }

    public List<Poi> searchQueryByName(String name, PageRequest pageRequest, float minScore) {
        log.info("request poi by name");
        return this.poiQueryRepository.searchPoiByName(name, pageRequest, minScore);
    }

    public Poi createPoi(CreatePoi.Request request) {
        String[] input = splitRequest(request.getFname(), request.getCname());
        return this.poiRepository.save(new Poi(request, input));
    }

    public void deletePoi(String poiId) {
        log.info("delete poi : {} ", poiId);
        if (this.poiRepository.deleteByPoiId(poiId) == 0) {
            throw new NotFoundPoiException();
        }
    }

    private String[] splitRequest(String fname, String cname) {
        return new String[]{
            fname + " " + cname,
            cname
        };
    }

    public UpdatePoi.Response updatePoi(String id, UpdatePoi.Request request) {
        Poi poi = this.poiRepository.findPoiByPoiId(id)
            .orElseThrow(NotFoundPoiException::new);
        String[] input = splitRequest(request.getFname(), request.getCname());
        Poi update = poi.update(request, input);
        Poi save = this.poiRepository.save(update);
        return new UpdatePoi.Response(save);
    }

    @Transactional(readOnly = true)
    public List<Poi> searchPoiByNameFilterPoiCodes(String name, String field, String category,
        float minScore,
        PageRequest of) {
        return this.poiQueryRepository.searchPoiByNameFilterPoiCodes(name, field, category, of,
            minScore);
    }
}
