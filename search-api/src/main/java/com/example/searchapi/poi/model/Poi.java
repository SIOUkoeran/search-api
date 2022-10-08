package com.example.searchapi.poi.model;


import com.example.searchapi.poi.dto.CreatePoi;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.CompletionField;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.core.suggest.Completion;

import javax.persistence.Embedded;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;


@Document(indexName = "poi")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Poi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String poi_id;

    private String poi_code;

    private String fname;

    private String cname;

    private int phone_a;

    private int phone_b;

    private int phone_c;

    private int zip_code;

    @Embedded
    private Location location;

    @CompletionField
    private Completion poi_suggest;

    public Poi(CreatePoi.Request request, String[] input) {
        this.poi_code = request.getPoiCode();
        this.fname = request.getFname();
        this.cname = request.getCname();
        this.phone_a = request.getPhoneA();
        this.phone_b = request.getPhoneB();
        this.phone_c = request.getPhoneC();
        this.zip_code = request.getZipCode();
        this.location = new Location(request.getLon(), request.getLan());
        this.poi_suggest = new Completion(input);
    }
}
