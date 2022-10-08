package com.example.searchapi.address.dto;

import com.example.searchapi.address.model.Address;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;

public class AddressDto {

    @NoArgsConstructor
    @Getter
    public static class RequestCreate{

        @JsonProperty("poi_code")
        @NotBlank(message = "poi_code 입력값이 비어있습니다.")
        private String poiCode;

        @NotBlank(message = "address 입력값이 비어있습니다.")
        private String address;

        @NotBlank(message = "primary_bun 입력값이 비어있습니다.")
        @JsonProperty("primary_bun")
        private int primaryBun;

        @NotBlank(message = "secondary_bun 입력값이 비어있습니다.")
        @JsonProperty("secondary_bun")
        private int secondaryBun;

        @JsonProperty("san_bun")
        private int sanBun;

        public RequestCreate(String poiCode, String address, int primaryBun, int secondaryBun, int sanBun) {
            this.poiCode = poiCode;
            this.address = address;
            this.primaryBun = primaryBun;
            this.secondaryBun = secondaryBun;
            this.sanBun = sanBun;
        }
    }

    @NoArgsConstructor
    @Getter
    public static class ResponseCreate{

        @JsonProperty("poi_id")
        private String poiId;

        @JsonProperty("poi_code")
        private String poiCode;

        private String address;

        @JsonProperty("primary_bun")
        private int primaryBun;

        @JsonProperty("secondary_bun")
        private int secondaryBun;

        @JsonProperty("san_bun")
        private int sanBun;

        public ResponseCreate(Address address) {
            this.poiId = address.getPoi_id();
            this.poiCode = address.getPoi_code();
            this.address = address.getAddress();
            this.primaryBun = address.getPrimary_bun();
            this.secondaryBun = address.getSecondary_bun();
            this.sanBun = address.getSan_bun();
        }
    }
}
