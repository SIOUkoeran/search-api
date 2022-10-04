package com.example.searchapi.poi.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class PoiSuggestDto {

    @NoArgsConstructor
    @Getter
    public static class Request{
        private String poi;

        public Request(String poi) {
            this.poi = poi;
        }
    }

    @NoArgsConstructor
    @Getter
    public static class Response{
        private List<String> suggestNameList;

        public Response(List<String> suggestNameList) {
            this.suggestNameList = suggestNameList;
        }
    }
}
