package com.example.carus.pdfrenderer;

import com.example.carus.pdfrenderer.interfaces.HtmlParser;
import com.example.carus.pdfrenderer.interfaces.PdfRenderer;
import com.example.carus.pdfrenderer.services.JsoupHtmlParserService;
import com.example.carus.pdfrenderer.services.OpenHtmlToPdfRendererService;
import org.apache.tika.Tika;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;

import java.io.IOException;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class OpenHtmlToPdfPdfRendererServiceTest {

    final PdfRenderer renderer = new OpenHtmlToPdfRendererService();
    final Tika tika = new Tika();
    final HtmlParser parser = new JsoupHtmlParserService();

    @Test
    void whenDetectPdfByTika_thenCorrect() throws IOException {
        Document document = parser.parse("<html lang=\"en\"><head><meta http-equiv=\"Content-Type\"" +
                " content=\"text/html; charset=UTF-8\" />" +
                "<title>Static HTML Page</title>  <meta name=\"viewport\" content=\"width=device-width, " +
                "initial-scale=1.0\"> </head><body></body></html>");
        byte[] pdf = renderer.renderPdf(document);
        boolean isPdf = Objects.equals(tika.detect(pdf), "application/pdf");
        assertTrue(isPdf);
    }
}
