package com.example.carus.pdfrenderer.controllers;

import com.example.carus.openapi.api.PdfRenderApiDelegate;
import com.example.carus.openapi.model.PdfRendererRequestDto;
import com.example.carus.pdfrenderer.interfaces.HtmlParser;
import com.example.carus.pdfrenderer.interfaces.HtmlValidator;
import com.example.carus.pdfrenderer.interfaces.PdfRenderer;
import com.example.carus.pdfrenderer.services.RequestLoggerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;

@Component
@Slf4j
public class PdfRendererController implements PdfRenderApiDelegate {
    private final PdfRenderer renderer;
    private final HtmlValidator htmlValidator;
    private final RequestLoggerService dbLogger;
    private final HtmlParser htmlParser;

    public PdfRendererController(PdfRenderer renderer,
                                 HtmlValidator htmlValidator, RequestLoggerService dbLogger, HtmlParser htmlParser) {
        this.renderer = renderer;
        this.htmlValidator = htmlValidator;
        this.dbLogger = dbLogger;
        this.htmlParser = htmlParser;
    }

    @Override
    public ResponseEntity<Resource> renderPdf(PdfRendererRequestDto pdfRendererRequestDto) {
        Document document = htmlParser.parse(pdfRendererRequestDto.getContent());
        log.info("HTML parsed successfully");
        htmlValidator.validate(document);
        log.info("HTML validated successfully");
        final byte[] pdf = renderer.renderPdf(document);
        log.info("PDF generated successfully");
        HttpHeaders headers = getPdfHeaders();
        dbLogger.logSuccessfulRequest();
        log.info("Sending PDF in response");
        ByteArrayResource resource = new ByteArrayResource(pdf);
        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
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