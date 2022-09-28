package com.example.searchapi.poi.model;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Location {

    private Float lon;
    private Float lat;
}
