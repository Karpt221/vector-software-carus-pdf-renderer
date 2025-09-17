package com.example.carus.pdfrenderer.utils.configs;

import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories(basePackages = "com.example.carus.pdfrenderer.repositories")
@EnableMongoAuditing
public class MongoDbConfig {
}
