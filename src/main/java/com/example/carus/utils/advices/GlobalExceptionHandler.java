package com.example.carus.utils.advices;

import com.example.carus.pdfrenderer.services.DbRequestLoggerService;
import com.example.carus.utils.enums.ExceptionMessage;
import com.example.carus.pdfrenderer.utils.enums.PdfRendererExceptionMessage;
import com.example.carus.pdfrenderer.utils.exceptions.HtmlValidationException;
import com.example.carus.pdfrenderer.utils.exceptions.PdfGenerationException;
import com.example.carus.utils.dtos.ErrorResponseDto;
import com.example.carus.utils.interfaces.HasMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    private final DbRequestLoggerService dbLogger;

    public GlobalExceptionHandler(DbRequestLoggerService dbLogger) {
        this.dbLogger = dbLogger;
    }

    @ExceptionHandler(HtmlValidationException.class)
    public ResponseEntity<ErrorResponseDto> handleHtmlValidationException(HtmlValidationException ex) {
        return handleErrorResponse(HttpStatus.BAD_REQUEST, ex, PdfRendererExceptionMessage.INVALID_HTML);
    }

    @ExceptionHandler(PdfGenerationException.class)
    public ResponseEntity<ErrorResponseDto> handlePdfGenerationException(PdfGenerationException ex) {
        return handleErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex, PdfRendererExceptionMessage.FAILED_TO_GENERATE_PDF);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleUnknownException(Exception ex) {
        return handleErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex, ExceptionMessage.SOMETHING_WRONG);
    }

    private ResponseEntity<ErrorResponseDto> handleErrorResponse(HttpStatus status, Exception ex,
                                                                 HasMessage clientMessage) {
        log.error("An error occurred while processing", ex);
        dbLogger.logErrorRequest(ex.toString());
        return new ResponseEntity<>(
                new ErrorResponseDto(status, clientMessage.getMessage()),
                status
        );
    }
}
