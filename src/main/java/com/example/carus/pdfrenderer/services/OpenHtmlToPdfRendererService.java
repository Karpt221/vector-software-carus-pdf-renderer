package com.example.carus.pdfrenderer.services;

import com.example.carus.pdfrenderer.utils.exceptions.PdfGenerationException;
import com.example.carus.pdfrenderer.interfaces.PdfRenderer;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.openhtmltopdf.svgsupport.BatikSVGDrawer;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;

import java.io.ByteArrayOutputStream;
import java.nio.file.FileSystems;

@Service
@Primary
public class OpenHtmlToPdfRendererService implements PdfRenderer {

    public byte[] renderPdf(Document document) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            String baseUrl = FileSystems.getDefault()
                    .getPath("src/main/resources/static/")
                    .toUri().toURL().toString();

            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useSVGDrawer(new BatikSVGDrawer());
            builder.withW3cDocument(document, baseUrl);
            builder.toStream(outputStream);
            builder.run();

            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new PdfGenerationException("Failed to generate PDF with OpenHTMLtoPDF", e);
        }
    }
}