package com.megayu.controller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.megayu")
public class MegayuEntry {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(MegayuEntry.class, args);
    }
}
