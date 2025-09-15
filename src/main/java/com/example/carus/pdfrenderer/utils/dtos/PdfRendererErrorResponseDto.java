package com.example.carus.pdfrenderer.utils.dtos;

import org.springframework.http.HttpStatus;

public record PdfRendererErrorResponseDto(HttpStatus status, String message, String cause) { }