package com.example.carus.pdfrenderer.controllers;

import com.example.carus.pdfrenderer.interfaces.HtmlParser;
import com.example.carus.pdfrenderer.interfaces.HtmlValidator;
import com.example.carus.pdfrenderer.interfaces.PdfRenderer;
import com.example.carus.pdfrenderer.services.DbRequestLoggerService;
import com.example.carus.utils.dtos.ErrorResponseDto;
import com.example.carus.pdfrenderer.utils.dtos.PdfRendererRequestDto;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.Document;


@RestController
@Slf4j
public class PdfRendererController {
    private final PdfRenderer renderer;
    private final HtmlValidator htmlValidator;
    private final DbRequestLoggerService dbLogger;
    private final HtmlParser htmlParser;

    public PdfRendererController(PdfRenderer renderer,
                                 HtmlValidator htmlValidator, DbRequestLoggerService dbLogger, HtmlParser htmlParser) {
        this.renderer = renderer;
        this.htmlValidator = htmlValidator;
        this.dbLogger = dbLogger;
        this.htmlParser = htmlParser;
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
                            schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(
                    responseCode = "500",
                    description = "Failed to generate PDF")
    })
    @PostMapping("/pdf-render")
    public ResponseEntity<?> renderPdf(@RequestBody PdfRendererRequestDto body) {
        Document document = htmlParser.parse(body.content());
        log.info("HTML parsed successfully");
        htmlValidator.validate(document);
        log.info("HTML validated successfully");
        final byte[] pdf = renderer.renderPdf(document);
        log.info("PDF generated successfully");
        HttpHeaders headers = getPdfHeaders();
        dbLogger.logSuccessfulRequest();
        log.info("Sending PDF in response");
        return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
    }

    private HttpHeaders getPdfHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        final String filename = "rendered-document.pdf";
        headers.setContentDispositionFormData(filename, filename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        return headers;
    }
}
