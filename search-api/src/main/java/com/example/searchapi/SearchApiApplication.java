package com.example.searchapi;

import com.example.searchapi.address.repository.AddressRepository;
import com.example.searchapi.category.repository.CategoryRepository;
import com.example.searchapi.poi.repository.PoiRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(excludeFilters = @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = {CategoryRepository.class, AddressRepository.class, PoiRepository.class}))
public class SearchApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(SearchApiApplication.class, args);
    }

}
