package com.example.searchapi.poi.dto;


import com.example.searchapi.poi.model.Poi;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class SearchPoi {

    @NoArgsConstructor
    @Getter
    public final static class Response {

        private List<Poi> poi;

        public Response(List<Poi> poi) {
            this.poi = poi;
        }
    }
}