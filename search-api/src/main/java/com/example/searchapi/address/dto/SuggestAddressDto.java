package com.example.searchapi.address.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
public class SuggestAddressDto {

    @Getter
    public static class Request {
        private final String address;

        public Request(String address) {
            this.address = address;
        }
    }

    @Getter
    public static class Response {
        private final List<String> address;

        public Response(List<String> address) {
            this.address = address;
        }
    }
}