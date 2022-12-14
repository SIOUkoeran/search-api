package com.example.searchapi.poi.model;


import com.example.searchapi.poi.dto.CreatePoi;
import com.example.searchapi.poi.dto.UpdatePoi;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Embedded;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.elasticsearch.common.UUIDs;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.CompletionField;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;


@Document(indexName = "poi")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Poi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Field
    private String id;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Field(name = "poi_id")
    private String poiId;

    private String poi_code;

    private String fname;

    private String cname;

    private int phone_a;

    private int phone_b;

    private int phone_c;

    private int zip_code;

    private String large_category;

    private String medium_category;

    private String small_category;

    @Embedded
    private Location location;

    @CompletionField(analyzer = "suggest_index_analyzer", searchAnalyzer = "suggest_search_analyzer")
    @JsonIgnore
    private String poi_suggest;

    public Poi(CreatePoi.Request request, String[] input) {
        this.poi_code = request.getPoiCode();
        this.fname = request.getFname();
        this.cname = request.getCname();
        this.phone_a = request.getPhoneA();
        this.phone_b = request.getPhoneB();
        this.phone_c = request.getPhoneC();
        this.zip_code = request.getZipCode();
        this.location = new Location(request.getLon(), request.getLan());
        this.poiId = UUIDs.randomBase64UUID();
//        poi_suggest = input[0];
    }

    public Poi update(UpdatePoi.Request request, String[] input) {
        this.poi_code = request.getPoiCode();
        this.fname = request.getFname();
        this.cname = request.getCname();
        this.phone_a = request.getPhoneA();
        this.phone_b = request.getPhoneB();
        this.phone_c = request.getPhoneC();
        this.zip_code = request.getZipCode();
        this.location = new Location(request.getLon(), request.getLan());
//        poi_suggest = input[0];
        return this;
    }
}
