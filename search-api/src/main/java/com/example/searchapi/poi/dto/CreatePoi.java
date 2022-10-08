package com.example.searchapi.poi.dto;


import com.example.searchapi.address.dto.CreateAddressDto;
import com.example.searchapi.poi.model.Poi;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CreatePoi {

    @NoArgsConstructor
    @Getter
    public static class Request{
        @JsonProperty("poi_code")
        @NotBlank(message = "poi_code 입력값이 비어있습니다.")
        private String poiCode;

        @NotBlank(message = "address 입력값이 비어있습니다.")
        private String address;

        @NotNull(message = "primary_bun 입력값이 비어있습니다.")
        @JsonProperty("primary_bun")
        private Integer primaryBun;

        @NotNull(message = "secondary_bun 입력값이 비어있습니다.")
        @JsonProperty("secondary_bun")
        private Integer secondaryBun;

        @JsonProperty("san_bun")
        private int sanBun;

        @NotBlank(message = "가게명이 비어있습니다.")
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

        @NotNull(message = "위도값이 비어있습니다.")
        private float lon;

        @NotNull(message = "경도값이 비어있습니다.")
        private float lan;

        public Request(String poiCode, String address, int primaryBun, int secondaryBun, int sanBun,
                       String fname, String cname, int phoneA, int phoneB, int phoneC, int zipCode, float lon, float lan) {
            this.poiCode = poiCode;
            this.address = address;
            this.primaryBun = primaryBun;
            this.secondaryBun = secondaryBun;
            this.sanBun = sanBun;
            this.fname = fname;
            this.cname = cname;
            this.phoneA = phoneA;
            this.phoneB = phoneB;
            this.phoneC = phoneC;
            this.zipCode = zipCode;
            this.lon = lon;
            this.lan = lan;
        }

        @Override
        public String toString() {
            return "Request{" +
                    "poiCode='" + poiCode + '\'' +
                    ", address='" + address + '\'' +
                    ", primaryBun=" + primaryBun +
                    ", secondaryBun=" + secondaryBun +
                    ", sanBun=" + sanBun +
                    ", fname='" + fname + '\'' +
                    ", cname='" + cname + '\'' +
                    ", phoneA=" + phoneA +
                    ", phoneB=" + phoneB +
                    ", phoneC=" + phoneC +
                    ", zipCode=" + zipCode +
                    ", lon=" + lon +
                    ", lan=" + lan +
                    '}';
        }
    }

    @NoArgsConstructor
    @Getter
    public static class Response{
        private Poi poi;
        private CreateAddressDto.Response address;

        public Response(Poi poi, CreateAddressDto.Response address) {
            this.poi = poi;
            this.address = address;
        }
    }
}
