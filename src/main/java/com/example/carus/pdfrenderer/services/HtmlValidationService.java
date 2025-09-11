package com.example.carus.pdfrenderer.services;

import com.example.carus.pdfrenderer.exceptions.HtmlValidationException;
import com.example.carus.pdfrenderer.interfaces.HtmlValidator;
import org.jsoup.Jsoup;
import org.jsoup.helper.W3CDom;
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

    public Document validate (String html){
        org.jsoup.nodes.Document jsopuDocument = Jsoup.parse(html);
        jsopuDocument.outputSettings().syntax(org.jsoup.nodes.Document.OutputSettings.Syntax.xml);
        Document document = W3CDom.convert(jsopuDocument);
        try {
            Validator validator = this.schema.newValidator();
            validator.validate(new DOMSource(document));
            return document;
        } catch (SAXException | IOException e) {
            throw new HtmlValidationException("Invalid HTML provided", e);
        }
    }
}
