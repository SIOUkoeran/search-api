package com.example.searchapi.address.model;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum AddressColumn {

    ADDRESS("address", 0),
    PRIMARYBUN("primary_bun", 1),
    SECONDATYBUN("secondary_bun", 2),
    SANBUN("san_bun", 3)
    ;

    private final String column;
    private final int    idx;

    AddressColumn(String column, int idx) {
        this.column = column;
        this.idx = idx;
    }
}
