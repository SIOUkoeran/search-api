package com.example.searchapi.poi.repository;

import com.example.searchapi.poi.model.Poi;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PoiRepository extends ElasticsearchRepository<Poi, String> {
}
