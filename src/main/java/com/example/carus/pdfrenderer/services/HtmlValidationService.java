package com.example.carus.pdfrenderer.services;

import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.Schema;
import javax.xml.validation.Validator;
import java.io.IOException;

@Service
public class HtmlValidationService {
    private final Schema schema;

    public HtmlValidationService(Schema schema) {
        this.schema = schema;
    }

    public void validate (Document document) throws IOException, SAXException {
        Validator validator = this.schema.newValidator();
        validator.validate(new DOMSource(document));
    }
}
