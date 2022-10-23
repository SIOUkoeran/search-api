package com.example.searchapi.address.dto;

import com.example.searchapi.address.model.Address;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class UpdateAddress {

    @NoArgsConstructor
    @Getter
    public static class Request {

        @JsonProperty("poi_code")
        @NotBlank(message = "poi_code 입력값이 비어있습니다.")
        private String poiCode;

        @NotBlank(message = "address 입력값이 비어있습니다.")
        private String address;

        @NotNull(message = "primary_bun 입력값이 비어있습니다.")
        @JsonProperty("primary_bun")
        private int primaryBun;

        @NotNull(message = "secondary_bun 입력값이 비어있습니다.")
        @JsonProperty("secondary_bun")
        private int secondaryBun;

        @JsonProperty("san_bun")
        private int sanBun;

        public Request(String poiCode, String address, int primaryBun, int secondaryBun,
            int sanBun) {
            this.poiCode = poiCode;
            this.address = address;
            this.primaryBun = primaryBun;
            this.secondaryBun = secondaryBun;
            this.sanBun = sanBun;
        }
    }

    @NoArgsConstructor
    @Getter
    public static class Response {

        private Address address;

        public Response(Address address) {
            this.address = address;
        }
    }
}
