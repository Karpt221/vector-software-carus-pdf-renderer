package com.example.carus.pdfrenderer.exceptions;

public class PdfGenerationException extends RuntimeException{
    public PdfGenerationException(String message, Throwable cause) {
        super(message, cause);
    }
}
