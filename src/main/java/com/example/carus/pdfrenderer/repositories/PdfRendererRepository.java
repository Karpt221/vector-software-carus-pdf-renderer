package com.example.carus.pdfrenderer.repositories;

import com.example.carus.pdfrenderer.models.Request;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PdfRendererRepository extends MongoRepository<Request, String> { }
