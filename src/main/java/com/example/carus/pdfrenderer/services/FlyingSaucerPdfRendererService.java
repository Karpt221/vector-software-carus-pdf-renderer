package com.example.carus.pdfrenderer.services;

import com.example.carus.pdfrenderer.utils.enums.PdfRendererExceptionMessage;
import com.example.carus.pdfrenderer.utils.exceptions.PdfGenerationException;
import com.example.carus.pdfrenderer.interfaces.PdfRenderer;
import org.springframework.stereotype.Service;

import org.w3c.dom.Document;
import org.xhtmlrenderer.layout.SharedContext;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.*;
import java.nio.file.FileSystems;

@Service
public class FlyingSaucerPdfRendererService implements PdfRenderer {

    public byte[] renderPdf(Document document) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ITextRenderer renderer = new ITextRenderer();
            SharedContext sharedContext = renderer.getSharedContext();
            sharedContext.setPrint(true);
            sharedContext.setInteractive(false);
            String baseUrl = FileSystems.getDefault()
                    .getPath("src/main/resources/static/")
                    .toUri().toURL().toString();
            renderer.setDocument(document, baseUrl);
            renderer.layout();
            renderer.createPDF(outputStream);

            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new PdfGenerationException(PdfRendererExceptionMessage.FAILED_TO_GENERATE_PDF.getMessage(), e);
        }
    }
}
