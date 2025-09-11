package com.example.carus.pdfrenderer.services;

import com.example.carus.pdfrenderer.exceptions.PdfGenerationException;
import com.example.carus.pdfrenderer.interfaces.PdfRenderer;
import org.springframework.stereotype.Service;

import org.w3c.dom.Document;
import org.xhtmlrenderer.layout.SharedContext;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.*;

@Service
public class PdfRendererService implements PdfRenderer {

    public byte[] renderPdf(Document document) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ITextRenderer renderer = new ITextRenderer();
            SharedContext sharedContext = renderer.getSharedContext();
            sharedContext.setPrint(true);
            sharedContext.setInteractive(false);

            renderer.setDocument(document, "");
            renderer.layout();
            renderer.createPDF(outputStream);

            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new PdfGenerationException("Failed to generate PDF", e);
        }
    }
}
