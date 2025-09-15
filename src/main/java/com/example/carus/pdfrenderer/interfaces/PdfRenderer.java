package com.example.carus.pdfrenderer.interfaces;

import com.example.carus.pdfrenderer.utils.exceptions.PdfGenerationException;
import org.w3c.dom.Document;

public interface PdfRenderer {
    byte[] renderPdf(Document document) throws PdfGenerationException;
}
