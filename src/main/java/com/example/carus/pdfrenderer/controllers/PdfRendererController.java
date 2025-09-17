package com.example.carus.pdfrenderer.controllers;

import com.example.carus.pdfrenderer.interfaces.HtmlParser;
import com.example.carus.pdfrenderer.services.RequestLoggerService;
import com.example.carus.pdfrenderer.utils.dtos.PdfRendererErrorResponseDto;
import com.example.carus.pdfrenderer.utils.dtos.PdfRendererRequestDto;
import com.example.carus.pdfrenderer.utils.exceptions.HtmlValidationException;
import com.example.carus.pdfrenderer.utils.exceptions.PdfGenerationException;
import com.example.carus.pdfrenderer.interfaces.HtmlValidator;
import com.example.carus.pdfrenderer.interfaces.PdfRenderer;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.Document;


@RestController
public class PdfRendererController {
    private final PdfRenderer renderer;
    private final HtmlValidator htmlValidator;
    private final RequestLoggerService requestLogger;
    private final HtmlParser htmlParser;

    PdfRendererController(PdfRenderer renderer,
                          HtmlValidator htmlValidator, RequestLoggerService requestLogger, HtmlParser htmlParser) {
        this.renderer = renderer;
        this.htmlValidator = htmlValidator;
        this.requestLogger = requestLogger;
        this.htmlParser = htmlParser;
    }

    @ExceptionHandler(HtmlValidationException.class)
    public ResponseEntity<PdfRendererErrorResponseDto> handleHtmlValidationException(HtmlValidationException ex) {
        requestLogger.logErrorRequest(ex.getMessage());
        return new ResponseEntity<>(
                new PdfRendererErrorResponseDto(HttpStatus.BAD_REQUEST, ex.getMessage(), ex.getCause().getMessage()),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(PdfGenerationException.class)
    public ResponseEntity<PdfRendererErrorResponseDto> handlePdfGenerationException(PdfGenerationException ex) {
        requestLogger.logErrorRequest(ex.getMessage());
        return new ResponseEntity<>(
                new PdfRendererErrorResponseDto(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), ex.getCause().getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    private HttpHeaders getPdfHeaders(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        final String filename = "rendered-document.pdf";
        headers.setContentDispositionFormData(filename, filename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        return headers;
    }

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "PDF document successfully rendered",
                    content = @Content(
                            mediaType = "application/pdf",
                            schema = @Schema(type = "string", format = "binary"))),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid HTML or document structure",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PdfRendererErrorResponseDto.class))),
            @ApiResponse(
                    responseCode = "500",
                    description = "Failed to generate PDF")
    })
    @PostMapping("/pdf-render")
    public ResponseEntity<?> renderPdf(@RequestBody PdfRendererRequestDto body){
        Document document = htmlParser.parse(body.content());
        htmlValidator.validate(document);
        final byte[] pdf = renderer.renderPdf(document);
        HttpHeaders headers = getPdfHeaders();
        requestLogger.logSuccessfulRequest();
        return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
    }
}
