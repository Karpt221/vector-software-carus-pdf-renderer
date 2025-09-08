package com.example.carus.pdfgenerator.controller;

import com.example.carus.pdfgenerator.dto.PdfGeneratorRequestDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PdfGeneratorController {
    @PostMapping("/pdf-render")
    public PdfGeneratorRequestDto pdfRender(@RequestBody PdfGeneratorRequestDto content) {
        return new PdfGeneratorRequestDto(String.format("This is your html %s!", content.content()));
    }
}
