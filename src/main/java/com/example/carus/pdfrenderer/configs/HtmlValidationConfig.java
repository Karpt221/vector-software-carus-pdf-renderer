package com.example.carus.pdfrenderer.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

@Configuration
public class HtmlValidationConfig {

    @Bean
    public Schema initializeSchema() throws URISyntaxException, IOException, SAXException {
        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        String schemaUrl = "https://www.w3.org/MarkUp/SCHEMA/xhtml11.xsd";
        try (InputStream stream = new URI(schemaUrl).toURL().openStream()) {
            Source schemaFile = new StreamSource(stream, schemaUrl);
            return factory.newSchema(schemaFile);
        }
    }
}