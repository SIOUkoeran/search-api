package com.example.searchapi.category.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Document(indexName = "poi")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Category {

    @Id
    private String id;

    private String poi_code;

    @Column(name = "large_category")
    private String large_category;

    @Column(name = "medium_category")
    private String medium_category;

    @Column(name = "small_category")
    private String small_category;


}
