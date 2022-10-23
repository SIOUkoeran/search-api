package com.example.searchapi.address.repository;

import com.example.searchapi.BaseTest;
import com.example.searchapi.address.model.Address;
import com.example.searchapi.common.query.QueryUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AddressQueryRepositoryImplTest extends BaseTest {

    @Autowired
    AddressQueryRepositoryImpl queryRepository;

    @Autowired
    QueryUtils queryUtils;

    Logger log = LoggerFactory.getLogger(AddressSuggestQueryRepositoryImplTest.class);

    @ParameterizedTest
    @ValueSource(strings = {"서울특별시 관악구 신림동 408-499"})
    @DisplayName("주소 오타 보정 (글자 1개 오타 시 정확도 체크)")
    void testAddressQueryWithFuzzy(String input) {
        String s = queryUtils.splitQueryReturnReverse(input);
        String[] strings = queryUtils.splitQueryUntilChar(input, '-');
        List<Address> addressesByAddress
            = this.queryRepository.findAddressesByAddress(input, s, strings, PageRequest.of(0, 10));
        addressesByAddress
            .forEach(address1 -> log.info(" {} {}-{} ", address1.getAddress()
                , address1.getPrimary_bun(), address1.getSecondary_bun()));

        Assertions.assertThat(addressesByAddress.get(0).getAddress())
            .isEqualTo("서울특별시 관악구 신림동");
        Assertions.assertThat(addressesByAddress.get(0).getPrimary_bun())
            .isEqualTo(808);
        Assertions.assertThat(addressesByAddress.get(0).getSecondary_bun())
            .isEqualTo(499);
    }
}