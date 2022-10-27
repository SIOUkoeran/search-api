package com.example.searchapi.address.model;

import com.example.searchapi.address.dto.AddressDto;
import com.example.searchapi.address.dto.CreateAddressDto;
import com.example.searchapi.address.dto.UpdateAddress;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.CompletionField;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;


@Document(indexName = "address")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Address {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Field(name = "poi_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String poiId;

    private String poi_code;
    private String address;
    private int san_bun;
    private int primary_bun;
    private int secondary_bun;

    @CompletionField(searchAnalyzer = "suggest_search_analyzer", analyzer = "suggest_index_analyzer")
    @JsonIgnore
    private String address_suggest;

    @Override
    public String toString() {
        return "Address{" +
            ", poi_id='" + id + '\'' +
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
        address_suggest = addressSuggest[0];
        this.poiId = poiId;
    }

    public Address update(UpdateAddress.Request request, String[] addressSuggest) {
        this.poi_code = request.getPoiCode();
        this.address = request.getAddress();
        this.san_bun = request.getSanBun();
        this.primary_bun = request.getPrimaryBun();
        this.secondary_bun = request.getSecondaryBun();
        this.address_suggest = address_suggest.toString();
        return this;
    }

    public Address(AddressDto.RequestCreate request, String[] addressSuggest, String poiId) {
        this.poi_code = request.getPoiCode();
        this.address = request.getAddress();
        this.san_bun = request.getSanBun();
        this.primary_bun = request.getPrimaryBun();
        this.secondary_bun = request.getSecondaryBun();
        this.id = poiId;
    }

    public void appendBun(StringBuilder sb) {
        if (primary_bun != 0) {
            sb.append(primary_bun);
            if (secondary_bun != 0) {
                sb.append("-").append(secondary_bun);
            }
        }
    }
}
