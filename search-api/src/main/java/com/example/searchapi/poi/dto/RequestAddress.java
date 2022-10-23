package com.example.searchapi.poi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.NotEmpty;

@NoArgsConstructor
@Getter
public class RequestAddress {

    @Getter
    public final static class address {

        @NotEmpty
        private String address;

        @JsonProperty("san_bun")
        private String sanBun;

        @JsonProperty("primary_bun")
        private String primaryBun;

        @JsonProperty("secondary_bun")
        private String secondaryBun;
    }
}
