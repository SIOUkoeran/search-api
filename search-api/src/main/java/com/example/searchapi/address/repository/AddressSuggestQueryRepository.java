package com.example.searchapi.address.repository;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressSuggestQueryRepository {
    List<String> suggestAddressByAddress(String address);
}
