package com.example.searchapi.address.service;

import com.example.searchapi.address.model.Address;
import com.example.searchapi.address.repository.AddressQueryRepository;
import com.example.searchapi.common.query.QueryPageUtils;
import com.example.searchapi.common.query.QueryUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class AddressService {

    private final AddressQueryRepository addressQueryRepository;
    private final QueryUtils queryUtils;

    public AddressService(AddressQueryRepository addressQueryRepository, QueryUtils queryUtils) {
        this.addressQueryRepository = addressQueryRepository;
        this.queryUtils = queryUtils;
    }

    @Transactional(readOnly = true)
    public List<String> searchPoiIdByAddress(String address, PageRequest pageRequest) {

        String reverseAddress  = queryUtils.splitQueryReturnReverse(address);
        String[] bun = queryUtils.splitQueryUntilChar(address, '-');

        return this.addressQueryRepository.findAddressesByAddress(address,
                        reverseAddress, bun, pageRequest)
                .stream()
                .map(Address::getPoi_id)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Address> searchAllAddressByAddress(String address, PageRequest pageRequest) {

        String reverseAddress = queryUtils.splitQueryReturnReverse(address);
        String[] bun = queryUtils.splitQueryUntilChar(address, '-');
        return this.addressQueryRepository.findAddressesByAddress(address, reverseAddress , bun, pageRequest);
    }

    @Transactional(readOnly = true)
    public Address searchAddressByPoiId(String poiId) {
        return this.addressQueryRepository.findAddressByPoiId(poiId);
    }

    @Transactional(readOnly = true)
    public Page<Address> searchFirstAllAddressByPoiCodes(List<String> poiCodes, int page, int size) {
        List<Address> addresses = this.addressQueryRepository.findFirstQueryAllAddressByPoiCodes(poiCodes);
        log.info("search result size : {}", addresses.size());
        return QueryPageUtils.convertListToPage(addresses, page, size);
    }

    @Transactional
    public Page<Address> searchNextAllAddressByPoiCodes(List<String> poiCodes, int page, int size, List<Object> searchAfter) {
        List<Address> addresses = this.addressQueryRepository.findNextQueryAllAddressByPoiCodes(poiCodes, searchAfter);
        return QueryPageUtils.convertListToPage(addresses, page, size);
    }

}
