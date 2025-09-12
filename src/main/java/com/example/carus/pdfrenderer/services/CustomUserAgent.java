package com.example.carus.pdfrenderer.services;

import org.xhtmlrenderer.resource.CSSResource;
import org.xhtmlrenderer.pdf.ITextOutputDevice;
import org.xhtmlrenderer.pdf.ITextUserAgent;
import org.xhtmlrenderer.resource.XMLResource;


/**
 * A custom UserAgentCallback for Flying Saucer that extends the default ITextUserAgent
 * to add support for rendering SVG images in the PDF.
 */
public class CustomUserAgent extends ITextUserAgent {

    private final String baseUrl;

    public CustomUserAgent(ITextOutputDevice outputDevice, String baseUrl) {
        super(outputDevice, 20);
        this.baseUrl = baseUrl;
    }


    @Override
    public CSSResource getCSSResource(String uri) {
        System.out.println("CSS: " + uri);
        return super.getCSSResource(uri);
    }

    @Override
    public XMLResource getXMLResource(String uri) {
        System.out.println("XML" + uri);
        return super.getXMLResource(uri);
    }

}
