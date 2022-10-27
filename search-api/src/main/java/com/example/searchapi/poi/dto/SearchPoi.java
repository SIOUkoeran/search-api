package com.example.searchapi.poi.dto;


import com.example.searchapi.address.model.Address;
import com.example.searchapi.poi.model.Poi;
import java.util.LinkedList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class SearchPoi {

    @NoArgsConstructor
    @Getter
    public final static class Response {

        private List<Poi> poi;

        public Response(List<Poi> poi) {
            this.poi = poi;
        }

        public Response(Poi poi) {
            if (poi == null) {
                this.poi = new LinkedList<Poi>();
                this.poi.add(poi);
            }
        }
    }

    @NoArgsConstructor
    @Getter
    public final static class ResponsePoi {

        private String poiId;
        private String fname;
        private String cname;
        private String address;
        private String largeCategory;
        private String mediumCategory;
        private String smallCategory;
        private String poiCode;
        private int primaryBun;
        private int secondaryBun;

        public ResponsePoi(Poi poi, Address address) {
            this.poiId = poi.getId();
            this.fname = poi.getFname();
            this.cname = poi.getCname();
            this.address = address.getAddress();
            this.largeCategory = poi.getLarge_category();
            this.mediumCategory = poi.getMedium_category();
            this.smallCategory = poi.getSmall_category();
            this.poiCode = poi.getPoi_code();
            this.primaryBun = address.getPrimary_bun();
            this.secondaryBun = address.getSecondary_bun();
        }
    }
}