package com.example.carus;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(basePackages = "com.example.carus.pdfrenderer.repositories")
@EnableMongoAuditing
public class CarusApplication {
    public static void main(String[] args) {
        SpringApplication.run(CarusApplication.class, args);
    }
}