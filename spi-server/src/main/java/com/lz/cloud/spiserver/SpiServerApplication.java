package com.lz.cloud.spiserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication
@ComponentScan(excludeFilters = {@ComponentScan.Filter(type = FilterType.REGEX,pattern = "com.lz.cloud.spiserver.service.jd.*")})
public class SpiServerApplication {


    public static void main(String[] args) {
        SpringApplication.run(SpiServerApplication.class, args);
    }

}
