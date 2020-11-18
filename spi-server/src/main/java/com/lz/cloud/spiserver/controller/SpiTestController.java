package com.lz.cloud.spiserver.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/spi")
public class SpiTestController {
    @RequestMapping("/test")
    public String spiTest(){
        return "SPI-Server";
    }

}
