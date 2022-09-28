package com.example.searchapi.poi.dto;


import com.example.searchapi.poi.model.Poi;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ResponseAddress {

    @Getter
    public static class address{

        private String fname;

        private String cname;

        @JsonProperty("phone_a")
        private String phoneA;

        @JsonProperty("phone_b")
        private String phoneB;

        @JsonProperty("phone_c")
        private String phoneC;

        @JsonProperty("zip_code")
        private String zipCode;


        public address(Poi POI) {
            this.fname = POI.getFname();
            this.cname = POI.getCname();
            this.phoneA = POI.getPhone_a();
            this.phoneB = POI.getPhone_b();
            this.phoneC = POI.getPhone_c();
            this.zipCode = POI.getZip_code();
        }
    }
}
