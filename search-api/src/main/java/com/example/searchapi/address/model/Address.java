package com.example.searchapi.address.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Document(indexName = "address")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Address {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String  id;

    private String  poi_id;
    private String  poi_code;
    private String  address;
    private int     san_bun;
    private int     primary_bun;
    private int     secondary_bun;

    @Override
    public String toString() {
        return "Address{" +
                "id='" + id + '\'' +
                ", poi_id='" + poi_id + '\'' +
                ", poi_code='" + poi_code + '\'' +
                ", address='" + address + '\'' +
                ", san_bun=" + san_bun +
                ", primary_bun=" + primary_bun +
                ", secondary_bun=" + secondary_bun +
                '}';
    }
}
