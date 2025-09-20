package com.example.carus.pdfrenderer;

import com.example.carus.openapi.api.PdfRenderApiController;
import com.example.carus.openapi.api.PdfRenderApiDelegate;
import com.example.carus.openapi.model.PdfRendererRequestDto;
import com.example.carus.pdfrenderer.services.RequestLoggerService;
import com.example.carus.pdfrenderer.exceptions.exceptions.HtmlValidationException;
import com.example.carus.pdfrenderer.exceptions.exceptions.PdfGenerationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PdfRenderApiController.class)
public class PdfRendererControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PdfRenderApiDelegate pdfRenderApiDelegate;
    @MockitoBean
    private RequestLoggerService requestLoggerService;

    @Test
    public void returnsPdfOnValidRequest() throws Exception {
        when(pdfRenderApiDelegate.renderPdf(any(PdfRendererRequestDto.class)))
                .thenReturn(ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_PDF)
                        .body(new ByteArrayResource("mock pdf content".getBytes())));

        String jsonPayload = "{\"content\":\"<html><body><p>Test</p></body></html>\"}";

        mockMvc.perform(post("/pdf-render")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_PDF))
                .andExpect(content().bytes("mock pdf content".getBytes()));
    }

    @Test
    public void returnsBadRequestWhenHtmlValidationFailed() throws Exception {
        final String errorMessage = "Invalid HTML provided";

        doThrow(new HtmlValidationException(errorMessage, new Exception("mock cause")))
                .when(pdfRenderApiDelegate).renderPdf(any());

        String jsonPayload = "{\"content\":\"<html><body><p>Test</p></body></html>\"}";

        mockMvc.perform(post("/pdf-render")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(errorMessage));
    }

    @Test
    public void returnsInternalServerErrorWhenRenderingFails() throws Exception {
        final String errorMessage = "Failed to generate PDF";

        doThrow(new PdfGenerationException(errorMessage, new Exception("mock cause")))
                .when(pdfRenderApiDelegate).renderPdf(any());

        String jsonPayload = "{\"content\":\"<html><body><p>Test</p></body></html>\"}";

        mockMvc.perform(post("/pdf-render")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value(errorMessage));
    }
}
