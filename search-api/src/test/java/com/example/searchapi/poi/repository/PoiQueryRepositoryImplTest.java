package com.example.searchapi.poi.repository;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PoiQueryRepositoryImplTest {

    @Test
    void reverseString() {
        String s = "abcd";
        for (int i = s.length() - 1; i >= 0; i--){
            System.out.println(s.charAt(i));
        }
    }

}