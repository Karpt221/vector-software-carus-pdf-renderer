package com.example.carus.advice;

import com.example.carus.pdfrenderer.dtos.PdfRendererErrorResponseDto;
import com.example.carus.pdfrenderer.exceptions.HtmlValidationException;
import com.example.carus.pdfrenderer.exceptions.PdfGenerationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HtmlValidationException.class)
    public ResponseEntity<PdfRendererErrorResponseDto> handleHtmlValidationException(HtmlValidationException ex) {
        return new ResponseEntity<>(
                new PdfRendererErrorResponseDto(HttpStatus.BAD_REQUEST, ex.getMessage(), ex.getCause().getMessage()),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(PdfGenerationException.class)
    public ResponseEntity<PdfRendererErrorResponseDto> handlePdfGenerationException(PdfGenerationException ex) {
        System.err.println("Error generating PDF: " + ex.getMessage());
        return new ResponseEntity<>(
                new PdfRendererErrorResponseDto(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), ex.getCause().getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}