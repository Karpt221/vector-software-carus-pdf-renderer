package com.example.carus.utils.dtos;

import org.springframework.http.HttpStatus;

public record ErrorResponseDto(HttpStatus status, String message) { }