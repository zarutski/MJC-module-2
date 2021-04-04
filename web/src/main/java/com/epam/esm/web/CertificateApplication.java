package com.epam.esm.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication(scanBasePackages = "com.epam.esm")
@EntityScan( basePackages = {"com.epam.esm.domain.entity"} )
public class CertificateApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(CertificateApplication.class, args);
    }
}