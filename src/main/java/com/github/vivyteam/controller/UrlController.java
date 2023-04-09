package com.github.vivyteam.controller;

import com.github.vivyteam.model.Url;
import com.github.vivyteam.service.UrlService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping("/url")
@Tag(
        name = "URL shortener",
        description = "URL shortener"
)
public class UrlController {
    private final UrlService urlService;

    @Autowired
    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @PostMapping("/shorten")
    @Operation(summary = "Shorten URL")
    public Mono<Url> shortenUrl(@Parameter(description = "Full URL") @RequestBody String urlFull) {
        return urlService.shortenUrl(urlFull);
    }

    @GetMapping("/get/{shortUrl}")
    public Mono<ResponseEntity<Url>> getFullUrl(@PathVariable String shortUrl) {
        return urlService.getFullUrl(shortUrl)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/redirect/{shortUrl}")
    public Mono<ResponseEntity<Void>> redirectToFullUrl(@PathVariable String shortUrl) {
        return urlService.getFullUrl(shortUrl)
                .map(v -> ResponseEntity
                        .status(HttpStatus.TEMPORARY_REDIRECT)
                        .header(HttpHeaders.LOCATION, v.getUrlFull().toString())
                        .build());
    }
}
