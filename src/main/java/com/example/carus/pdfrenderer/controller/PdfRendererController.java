package com.example.carus.pdfrenderer.controller;

import com.example.carus.pdfrenderer.dto.PdfRendererRequestDto;
import com.example.carus.pdfrenderer.service.PdfRendererService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class PdfRendererController {
    private  final PdfRendererService service;

    PdfRendererController(PdfRendererService service) {
        this.service = service;
    }

    @PostMapping("/pdf-render")
    public ResponseEntity<byte[]> pdfRender(@RequestBody PdfRendererRequestDto body) {
        return service.pdfRender(body.content());
    }
}
