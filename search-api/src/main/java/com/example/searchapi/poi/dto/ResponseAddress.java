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
        private int phoneA;

        @JsonProperty("phone_b")
        private int phoneB;

        @JsonProperty("phone_c")
        private int phoneC;

        @JsonProperty("zip_code")
        private int zipCode;


        public address(CreatePoi.Request request) {
            this.fname = request.getFname();
            this.cname = request.getCname();
            this.phoneA = request.getPhoneA();
            this.phoneB = request.getPhoneB();
            this.phoneC = request.getPhoneC();
            this.zipCode = request.getZipCode();
        }
    }
}
