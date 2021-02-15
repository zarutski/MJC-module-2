package com.epam.esm.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.epam.esm")
public class CertificateApplication {

    public static void main(String[] args) {
        SpringApplication.run(CertificateApplication.class, args);
    }
}