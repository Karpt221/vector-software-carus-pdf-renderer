package com.example.carus.pdfrenderer.dtos;

import org.springframework.http.HttpStatus;

public record PdfRendererErrorResponseDto(HttpStatus status, String message, String cause) { }