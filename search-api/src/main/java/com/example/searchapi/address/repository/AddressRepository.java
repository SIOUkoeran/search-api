package com.example.searchapi.address.repository;

import com.example.searchapi.address.model.Address;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends ElasticsearchRepository<Address, String> {

}
