package com.example.carus.pdfrenderer.exceptions;

import com.example.carus.pdfrenderer.services.RequestLoggerService;
import com.example.carus.pdfrenderer.exceptions.exceptions.HtmlValidationException;
import com.example.carus.pdfrenderer.exceptions.exceptions.PdfGenerationException;
import com.example.carus.pdfrenderer.utils.dtos.ErrorResponseDto;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    private final RequestLoggerService dbLogger;

    public GlobalExceptionHandler(RequestLoggerService dbLogger) {
        this.dbLogger = dbLogger;
    }

    @ExceptionHandler(HtmlValidationException.class)
    public ResponseEntity<ErrorResponseDto> handleHtmlValidationException(HtmlValidationException ex) {
        return handleErrorResponse(HttpStatus.BAD_REQUEST, ex);
    }

    @ExceptionHandler(PdfGenerationException.class)
    public ResponseEntity<ErrorResponseDto> handlePdfGenerationException(PdfGenerationException ex) {
        return handleErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class, MethodArgumentNotValidException.class,
            UnrecognizedPropertyException.class})
    public ResponseEntity<ErrorResponseDto> handleMalformedRequestExceptions(Exception ex) {
        return handleErrorResponse(HttpStatus.BAD_REQUEST, ex);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleOtherExceptions(Exception ex) {
        return handleErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex);
    }

    private ResponseEntity<ErrorResponseDto> handleErrorResponse(HttpStatus status, Exception ex) {
        log.error("An error occurred while processing", ex);
        dbLogger.logErrorRequest(ex.toString());
        return new ResponseEntity<>(
                new ErrorResponseDto(status, ex.getMessage(), ex.getCause() == null ? "" : ex.getCause().getMessage()),
                status
        );
    }
}
