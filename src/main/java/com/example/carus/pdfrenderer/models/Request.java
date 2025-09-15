package com.example.carus.pdfrenderer.models;

import com.example.carus.pdfrenderer.utils.enums.RequestStatus;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document("requests")
public class Request {
    @Id
    private String id;

    @CreatedDate
    @Indexed(expireAfter = "7d")
    private Date createdDate;
    private RequestStatus status;
    private String errorMessage;

    public Request(RequestStatus status) {
        this.status = status;
        this.errorMessage = null;
    }

    public Request(RequestStatus status, String errorMessage) {
        this.status = status;
        this.errorMessage = errorMessage;
    }
}