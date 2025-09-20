package com.example.carus.pdfrenderer.interfaces;

import com.example.carus.pdfrenderer.exceptions.exceptions.HtmlValidationException;
import org.w3c.dom.Document;

public interface HtmlValidator {
    void validate (Document document) throws HtmlValidationException;
}
