package com.example.searchapi.poi.dto;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class PoiSuggestDto {

    @NoArgsConstructor
    @Getter
    public static class Request {

        private String poi;

        public Request(String poi) {
            this.poi = poi;
        }

        public void replaceAll(String regex, String replacement) {
            this.poi = this.poi.replaceAll(regex, replacement);
        }
    }

    @NoArgsConstructor
    @Getter
    public static class Response {

        private List<String> suggestNameList;

        public Response(List<String> suggestNameList) {
            this.suggestNameList = suggestNameList;
        }
    }
}
