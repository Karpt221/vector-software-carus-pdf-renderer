package com.example.carus.pdfrenderer.utils.enums;

import com.example.carus.utils.interfaces.HasMessage;

public enum PdfRendererExceptionMessage implements HasMessage {
    INVALID_HTML("Invalid HTML provided"),
    FAILED_TO_GENERATE_PDF("Failed to generate PDF");

    private final String message;

    PdfRendererExceptionMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
