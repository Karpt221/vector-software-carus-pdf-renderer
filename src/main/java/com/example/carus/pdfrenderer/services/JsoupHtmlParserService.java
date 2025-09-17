package com.example.carus.pdfrenderer.services;

import com.example.carus.pdfrenderer.interfaces.HtmlParser;
import org.jsoup.Jsoup;
import org.jsoup.helper.W3CDom;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;

@Service
public class JsoupHtmlParserService implements HtmlParser {

    public Document parse(String html) {
        org.jsoup.nodes.Document jsopuDocument = Jsoup.parse(html);
        jsopuDocument.outputSettings().syntax(org.jsoup.nodes.Document.OutputSettings.Syntax.xml);
        return W3CDom.convert(jsopuDocument);
    }
}
