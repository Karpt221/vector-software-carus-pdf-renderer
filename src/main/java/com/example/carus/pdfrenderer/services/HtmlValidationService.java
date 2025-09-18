package com.example.carus.pdfrenderer.services;

import com.example.carus.pdfrenderer.utils.enums.PdfRendererExceptionMessage;
import com.example.carus.pdfrenderer.utils.exceptions.HtmlValidationException;
import com.example.carus.pdfrenderer.interfaces.HtmlValidator;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.Schema;
import javax.xml.validation.Validator;
import java.io.IOException;

@Service
public class HtmlValidationService implements HtmlValidator {
    private final Schema schema;

    public HtmlValidationService(Schema schema) {
        this.schema = schema;
    }

    public void validate (Document document) throws HtmlValidationException {
        try {
            Validator validator = this.schema.newValidator();
            validator.validate(new DOMSource(document));
        } catch (SAXException | IOException e) {
            throw new HtmlValidationException(PdfRendererExceptionMessage.INVALID_HTML.getMessage(), e);
        }
    }
}
