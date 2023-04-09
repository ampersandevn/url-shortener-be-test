package com.github.vivyteam.model;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Document
@Schema(description = "URL")
public class Url {

    private String urlFull;
    @Id
    private String urlShort;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;

    public Url(String urlFull, String urlShort, LocalDate date) {
        this.urlFull = urlFull;
        this.urlShort = urlShort;
        this.date = date;
    }

    public String getUrlFull() {
        return urlFull;
    }

    public void setUrlFull(String urlFull) {
        this.urlFull = urlFull;
    }

    public String getUrlShort() {
        return urlShort;
    }

    public void setUrlShort(String urlShort) {
        this.urlShort = urlShort;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
