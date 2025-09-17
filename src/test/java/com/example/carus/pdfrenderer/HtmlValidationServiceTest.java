package com.example.carus.pdfrenderer;

import com.example.carus.pdfrenderer.interfaces.HtmlParser;
import com.example.carus.pdfrenderer.services.HtmlValidationService;
import com.example.carus.pdfrenderer.services.JsoupHtmlParserService;
import com.example.carus.pdfrenderer.utils.configs.HtmlValidationConfig;
import com.example.carus.pdfrenderer.utils.exceptions.HtmlValidationException;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class HtmlValidationServiceTest {
    HtmlValidationConfig config = new HtmlValidationConfig();
    HtmlValidationService validator = new HtmlValidationService(config.initializeSchema());;
    HtmlParser parser = new JsoupHtmlParserService();

    public HtmlValidationServiceTest() throws URISyntaxException, IOException, SAXException {
    }

    @Test
    public void doesNothingWhenValidHtml() {
        Document document = parser.parse("<html lang=\"en\"><head><meta http-equiv=\"Content-Type\"" +
                " content=\"text/html; charset=UTF-8\" />" +
                "<title>Static HTML Page</title>  <meta name=\"viewport\" content=\"width=device-width, " +
                "initial-scale=1.0\"> </head><body></body></html>");
        assertDoesNotThrow(() -> validator.validate(document));
    }

    @Test
    public void throwsExceptionWhenInvalidHtml() {
        Document document = parser.parse("<invalid-tag>Hello</invalid-tag>");
        assertThrows(HtmlValidationException.class, () -> validator.validate(document));
    }
}
