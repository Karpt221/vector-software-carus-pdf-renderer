package com.example.carus.pdfrenderer.utils.dtos;

import org.springframework.http.HttpStatus;

public record ErrorResponseDto(HttpStatus status, String message) { }