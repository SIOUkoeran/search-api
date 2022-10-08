package com.example.searchapi.address.model;

import com.example.searchapi.address.dto.CreateAddressDto;
import com.example.searchapi.address.dto.UpdateAddress;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.CompletionField;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.core.suggest.Completion;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;


@Document(indexName = "address")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Address {


    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String  poi_id;

    private String  poi_code;
    private String  address;
    private int     san_bun;
    private int     primary_bun;
    private int     secondary_bun;

    @CompletionField()
    private Completion address_suggest;

    @Override
    public String toString() {
        return "Address{" +
                ", poi_id='" + poi_id + '\'' +
                ", poi_code='" + poi_code + '\'' +
                ", address='" + address + '\'' +
                ", san_bun=" + san_bun +
                ", primary_bun=" + primary_bun +
                ", secondary_bun=" + secondary_bun +
                '}';
    }

    public Address(CreateAddressDto.Request request, String[] addressSuggest, String poiId) {
        this.poi_code = request.getPoiCode();
        this.address = request.getAddress();
        this.san_bun = request.getSanBun();
        this.primary_bun = request.getPrimaryBun();
        this.secondary_bun = request.getSecondaryBun();
        this.poi_id = poiId;
        this.address_suggest = new Completion(addressSuggest);
    }

    public Address update(UpdateAddress.Request request, String[] addressSuggest) {
        this.poi_code = request.getPoiCode();
        this.address = request.getAddress();
        this.san_bun = request.getSanBun();
        this.primary_bun = request.getPrimaryBun();
        this.secondary_bun = request.getSecondaryBun();
        this.address_suggest = new Completion(addressSuggest);
        return this;
    }
}
