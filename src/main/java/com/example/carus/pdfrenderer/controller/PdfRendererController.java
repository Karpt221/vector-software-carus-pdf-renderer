package com.example.carus.pdfrenderer.controller;

import com.example.carus.pdfrenderer.dto.PdfRendererRequestDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PdfRendererController {
    @PostMapping("/pdf-render")
    public PdfRendererRequestDto pdfRender(@RequestBody PdfRendererRequestDto body) {
        return new PdfRendererRequestDto(String.format("This is your html %s!", body.content()));
    }
}
