package com.example.carus.pdfrenderer.services;

import com.example.carus.pdfrenderer.models.Request;
import com.example.carus.pdfrenderer.repositories.PdfRendererRepository;
import com.example.carus.pdfrenderer.utils.enums.RequestStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RequestLoggerService {

    @Autowired
    private PdfRendererRepository repository;

    public void logSuccessfulRequest() {
        Request newRequest = new Request(RequestStatus.SUCCESS);
        repository.save(newRequest);
    }

    public void logErrorRequest(String errorMessage) {
        Request newRequest = new Request(RequestStatus.FAILED, errorMessage);
        repository.save(newRequest);
    }
}