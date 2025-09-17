package com.example.carus.pdfrenderer;

import com.example.carus.pdfrenderer.controllers.PdfRendererController;
import com.example.carus.pdfrenderer.interfaces.HtmlParser;
import com.example.carus.pdfrenderer.interfaces.HtmlValidator;
import com.example.carus.pdfrenderer.interfaces.PdfRenderer;
import com.example.carus.pdfrenderer.services.JsoupHtmlParserService;
import com.example.carus.pdfrenderer.services.RequestLoggerService;
import com.example.carus.pdfrenderer.utils.exceptions.HtmlValidationException;
import com.example.carus.pdfrenderer.utils.exceptions.PdfGenerationException;
import org.jsoup.Jsoup;
import org.jsoup.helper.W3CDom;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.w3c.dom.Document;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PdfRendererController.class)
public class PdfRendererControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PdfRenderer pdfRenderer;
    @MockitoBean
    private HtmlValidator htmlValidator;
    @MockitoBean
    private HtmlParser htmlParser;
    @MockitoBean
    private RequestLoggerService requestLogger;

    HtmlParser parser = new JsoupHtmlParserService();

    @Test
    public void returnsPdfOnValidRequest() throws Exception {
        Document mockDocument = parser.parse("mock document");

        when(htmlParser.parse(any(String.class))).thenReturn(mockDocument);

        doNothing().when(htmlValidator).validate(any(Document.class));

        byte[] mockPdf = "mock pdf content".getBytes();
        when(pdfRenderer.renderPdf(any(Document.class))).thenReturn(mockPdf);

        String jsonPayload = "{\"content\":\"<html><body><p>Test</p></body></html>\"}";

        mockMvc.perform(post("/pdf-render")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_PDF))
                .andExpect(content().bytes(mockPdf));
    }

    @Test
    public void returnsBadRequestWhenHtmlValidationFailed() throws Exception {
        Document mockDocument = parser.parse("mock document");

        when(htmlParser.parse(any(String.class))).thenReturn(mockDocument);

        final String errorMessage = "Invalid HTML provided";
        doThrow(new HtmlValidationException(errorMessage, new Exception("mock cause")))
                .when(htmlValidator).validate(mockDocument);

        String jsonPayload = "{\"content\":\"<html><body><p>Test</p></body></html>\"}";

        mockMvc.perform(post("/pdf-render")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.message")
                        .value(errorMessage));
    }

    @Test
    public void returnsInternalServerErrorWhenRenderingFails() throws Exception {
        Document mockDocument = parser.parse("mock document");

        when(htmlParser.parse(any(String.class))).thenReturn(mockDocument);

        doNothing().when(htmlValidator).validate(any(Document.class));

        final String errorMessage = "Failed to generate PDF with OpenHTMLtoPDF";
        doThrow(new PdfGenerationException(errorMessage, new Exception("mock cause")))
                .when(pdfRenderer).renderPdf(any(Document.class));

        String jsonPayload = "{\"content\":\"<html><body><p>Test</p></body></html>\"}";

        mockMvc.perform(post("/pdf-render")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isInternalServerError()).andExpect(jsonPath("$.message")
                        .value(errorMessage));
    }
}
