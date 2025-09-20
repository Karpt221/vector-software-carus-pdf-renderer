package com.example.carus.pdfrenderer;

import com.example.carus.pdfrenderer.interfaces.HtmlParser;
import com.example.carus.pdfrenderer.services.JsoupHtmlParserService;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import static org.junit.jupiter.api.Assertions.*;

public class HtmlParserServiceTest {

    private final HtmlParser htmlParser = new JsoupHtmlParserService();

    @Test
    void parseSimpleHtml() {
        String html = "<html><body><h1>Hello, World!</h1></body></html>";
        Document doc = htmlParser.parse(html);
        NodeList h1Elements = doc.getElementsByTagName("h1");
        assertEquals(1, h1Elements.getLength());
        assertEquals("Hello, World!", h1Elements.item(0).getTextContent());
    }

    @Test
    void parseHtmlWithAttributes() {
        String html = "<html><body><a href=\"/test\">Test Link</a></body></html>";
        Document doc = htmlParser.parse(html);
        NodeList aElements = doc.getElementsByTagName("a");
        assertEquals(1, aElements.getLength());
        assertEquals("/test", aElements.item(0).getAttributes().getNamedItem("href").getNodeValue());
    }

}