package com.example.carus.pdfrenderer.services;

import com.example.carus.pdfrenderer.exceptions.HtmlValidationException;
import com.example.carus.pdfrenderer.exceptions.PdfGenerationException;
import org.jsoup.Jsoup;
import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import org.xhtmlrenderer.layout.SharedContext;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.xml.sax.SAXException;

import java.io.*;

@Service
public class PdfRendererService {
    private final HtmlValidationService htmlValidator;

    PdfRendererService(HtmlValidationService htmlValidator) {
        this.htmlValidator = htmlValidator;
    }

    public byte[] pdfRender(String html) {
        Document document = Jsoup.parse(html);
        document.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
        try {
            htmlValidator.validate(W3CDom.convert(document));
        } catch (SAXException | IOException e) {
            throw new HtmlValidationException("Invalid HTML provided", e);
        }
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ITextRenderer renderer = new ITextRenderer();
            SharedContext sharedContext = renderer.getSharedContext();
            sharedContext.setPrint(true);
            sharedContext.setInteractive(false);

            renderer.setDocumentFromString(document.html());
            renderer.layout();
            renderer.createPDF(outputStream);

            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new PdfGenerationException("Failed to generate PDF", e);
        }
    }
}
