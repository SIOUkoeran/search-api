package com.example.searchapi.poi.model;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.Embedded;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Document(indexName = "poi")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Poi {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    private String poi_id;

    private String poi_code;

    private String fname;

    private String cname;

    private String phone_a;

    private String phone_b;

    private String phone_c;

    private String zip_code;

    @Embedded
    private Location location;

}
