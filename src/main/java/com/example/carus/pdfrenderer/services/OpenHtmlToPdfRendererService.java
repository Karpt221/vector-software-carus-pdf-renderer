package com.example.carus.pdfrenderer.services;

import com.example.carus.pdfrenderer.interfaces.PdfRenderer;
import com.example.carus.pdfrenderer.utils.enums.PdfRendererExceptionMessage;
import com.example.carus.pdfrenderer.exceptions.exceptions.PdfGenerationException;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.openhtmltopdf.svgsupport.BatikSVGDrawer;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;

import java.io.ByteArrayOutputStream;
import java.nio.file.FileSystems;

@Service
@Profile("open_html_to_pdf")
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
            throw new PdfGenerationException(PdfRendererExceptionMessage.FAILED_TO_GENERATE_PDF.getMessage(), e);
        }
    }
}