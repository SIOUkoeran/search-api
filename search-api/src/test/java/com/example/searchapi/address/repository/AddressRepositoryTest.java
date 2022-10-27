package com.example.searchapi.address.repository;

import com.example.searchapi.address.model.Address;
import com.example.searchapi.common.query.QueryUtils;
import com.example.searchapi.container.IndexConfig;
import com.example.searchapi.container.TestContainer;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;

@Import({TestContainer.class})
@SpringBootTest
class AddressRepositoryTest {


    @Autowired
    IndexConfig indexConfig;

    @Autowired
    ElasticsearchOperations operations;

    @Autowired
    AddressQueryRepository addressQueryRepository;

    @Autowired
    QueryUtils queryUtils;

    @Autowired
    RestHighLevelClient client;

    Logger logger = LoggerFactory.getLogger(AddressRepositoryTest.class);

    @Test
    void testAddressByAddressQuery() {
        String s = "서울특별시 신림 808-499";
        String reverse = queryUtils.splitQueryReturnReverse(s);
        String[] strings = queryUtils.splitQueryUntilChar(s, '-');
        List<Address> addressesByAddress =
            this.addressQueryRepository
                .findAddressesByAddress(s, reverse, strings, PageRequest.of(0, 10));
        addressesByAddress.forEach(
            address -> logger.info("{} : {} {}", address.getAddress(), address.getPrimary_bun(),
                address.getSecondary_bun())
        );
        Address highestScoreAddress = addressesByAddress.get(0);
        Assertions.assertThat(highestScoreAddress.getPrimary_bun()).isEqualTo(808);
        Assertions.assertThat(highestScoreAddress.getSecondary_bun()).isEqualTo(499);
    }

}