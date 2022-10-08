package com.example.searchapi.address.repository;

import com.example.searchapi.address.model.Address;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressQueryRepository {

//    List<Address> findAddressesByAddress(String address, PageRequest pageRequest);
    List<Address> findAddressesByAddress(String address, String reversedAddressArray,
                                         String[] bun, PageRequest pageRequest);


    List<Address> findAddressesByBun(String primary_bun, String secondary_bun, PageRequest pageRequest);
    Address findAddressByPoiId(String poiId);
    List<Address> findAll();
    List<Address> findFirstQueryAllAddressByPoiCodes(List<String> poiCodes);
    List<Address> findNextQueryAllAddressByPoiCodes(List<String> poiCodes, List<Object> searchAfter);
}
