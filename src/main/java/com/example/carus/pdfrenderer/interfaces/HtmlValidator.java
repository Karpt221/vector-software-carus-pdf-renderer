package com.example.carus.pdfrenderer.interfaces;

import com.example.carus.pdfrenderer.utils.exceptions.HtmlValidationException;
import org.w3c.dom.Document;

public interface HtmlValidator {
    Document validate (String html) throws HtmlValidationException;
}
