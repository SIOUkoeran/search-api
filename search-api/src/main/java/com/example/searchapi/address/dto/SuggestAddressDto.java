package com.example.searchapi.address.dto;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@SuppressWarnings("checkstyle:MissingJavadocType")
public class SuggestAddressDto {


    @NoArgsConstructor
    @Getter
    public static class Request {

        private String address;

        public Request(String address) {
            this.address = address.replaceAll(" ", "");
        }

        public void addressReplace(String regex, String replacement) {
            this.address = this.address.replaceAll(regex, replacement);
        }
    }

    @Getter
    @NoArgsConstructor
    public static class Response {

        private List<String> address;

        public Response(List<String> address) {
            this.address = address;
        }
    }
}
