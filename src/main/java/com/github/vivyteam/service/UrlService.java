package com.github.vivyteam.service;

import com.github.vivyteam.model.Url;
import reactor.core.publisher.Mono;

public interface UrlService {
    /**
     * Shortens URL
     * @param urlFull
     * @return
     */
    Mono<Url> shortenUrl(String urlFull);

    /**
     * Get full URL by short form
     * @param shortFull
     * @return
     */
    Mono<Url> getFullUrl(String shortFull);

    /**
     * Redirect to full URL by short form
     * @param shortFull
     * @return
     */
    Mono<Url> redirectToFullUrl(String shortFull);
}
