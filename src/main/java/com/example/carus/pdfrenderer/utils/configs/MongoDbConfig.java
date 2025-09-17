package com.example.carus.pdfrenderer.utils.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.example.carus.pdfrenderer.repositories")
@EnableMongoAuditing
public class MongoDbConfig {
}
