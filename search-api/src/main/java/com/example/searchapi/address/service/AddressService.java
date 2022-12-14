package com.example.searchapi.address.service;

import com.example.searchapi.address.dto.CreateAddressDto;
import com.example.searchapi.address.dto.UpdateAddress;
import com.example.searchapi.address.exception.NotFoundAddressException;
import com.example.searchapi.address.model.Address;
import com.example.searchapi.address.repository.AddressQueryRepository;
import com.example.searchapi.address.repository.AddressRepository;
import com.example.searchapi.common.query.QueryPageUtils;
import com.example.searchapi.common.query.QueryUtils;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class AddressService {

    private final AddressQueryRepository addressQueryRepository;
    private final QueryUtils queryUtils;
    private final AddressRepository addressRepository;

    public AddressService(AddressQueryRepository addressQueryRepository, QueryUtils queryUtils,
        AddressRepository addressRepository) {
        this.addressQueryRepository = addressQueryRepository;
        this.queryUtils = queryUtils;
        this.addressRepository = addressRepository;
    }

    @Transactional(readOnly = true)
    public List<String> searchPoiIdByAddress(String address, PageRequest pageRequest) {

        String reverseAddress = queryUtils.splitQueryReturnReverse(address);
        String[] bun = queryUtils.splitQueryUntilChar(address, '-');

        return this.addressQueryRepository.findAddressesByAddress(address,
                reverseAddress, bun, pageRequest)
            .stream()
            .map(Address::getId)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Address> searchAllAddressByAddress(String address, PageRequest pageRequest) {

        String reverseAddress = queryUtils.splitQueryReturnReverse(address);
        String[] bun = queryUtils.splitQueryUntilChar(address, '-');
        return this.addressQueryRepository.findAddressesByAddress(address, reverseAddress, bun,
            pageRequest);
    }

    @Transactional(readOnly = true)
    public Address searchAddressByPoiId(String poiId) {
        return this.addressRepository.findAddressByPoiId(poiId)
            .orElseThrow(NotFoundAddressException::new);
    }

    @Transactional(readOnly = true)
    public Page<Address> searchFirstAllAddressByPoiCodes(List<String> poiCodes, int page,
        int size) {
        List<Address> addresses = this.addressQueryRepository.findFirstQueryAllAddressByPoiCodes(
            poiCodes);
        log.info("search result size : {}", addresses.size());
        return QueryPageUtils.convertListToPage(addresses, page, size);
    }

    @Transactional(readOnly = true)
    public Page<Address> searchNextAllAddressByPoiCodes(List<String> poiCodes, int page, int size,
        List<Object> searchAfter) {
        List<Address> addresses = this.addressQueryRepository.findNextQueryAllAddressByPoiCodes(
            poiCodes, searchAfter);
        return QueryPageUtils.convertListToPage(addresses, page, size);
    }

    public CreateAddressDto.Response createAddress(CreateAddressDto.Request request, String poiId) {

        String[] address = splitAddress(request.getAddress(), request.getPrimaryBun(),
            request.getSecondaryBun());
        Address savedAddress = this.addressRepository.save(new Address(request, address, poiId));
        log.info("{}", savedAddress);
        return new CreateAddressDto.Response(savedAddress);
    }

    public void deleteAddress(String poiId) {
        if (this.addressRepository.deleteByPoiId(poiId) == 0) {
            throw new NotFoundAddressException();
        }
    }

    /**
     * ??????????????? ????????? ????????? ????????? ????????? 610-50 ????????? ????????? 610-50 ????????? 610-50 610-50
     */
    private String[] splitAddress(String addressString, int primaryBun, int secondaryBun) {
        String[] input = new String[4];

        String[] address = addressString.split(" ");
        for (int i = 0; i < 3; i++) {
            input[i] = address[i];
        }
        input[3] = String.valueOf(primaryBun)
            + '-'
            + String.valueOf(secondaryBun);

        String[] result = new String[4];
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            sb.setLength(0);
            for (int j = i; j < 4; j++) {
                sb.append(input[j]).append(" ");
            }
            result[i] = String.valueOf(sb);
        }
        return result;
    }

    public UpdateAddress.Response updateAddress(String id, UpdateAddress.Request request) {

        Address findAddress = this.addressRepository.findAddressByPoiId(id)
            .orElseThrow(NotFoundAddressException::new);
        String[] input
            = splitAddress(request.getAddress(), request.getPrimaryBun(),
            request.getSecondaryBun());
        Address update = findAddress.update(request, input);
        return new UpdateAddress.Response(this.addressRepository.save(update));
    }
}
