package com.example.searchapi.poi.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Location {

    private Float lon;
    private Float lat;

    public Location(float lon, float lat) {
        this.lon = lon;
        this.lat = lat;
    }
}
