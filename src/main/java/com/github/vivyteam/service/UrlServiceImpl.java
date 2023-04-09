package com.github.vivyteam.service;

import com.github.vivyteam.model.Url;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class UrlServiceImpl implements UrlService {
    private static final String URL_PREFIX = "https://myservicedomain.de/";
    private static final Integer SHORT_URL_LENGTH = 8;
    private final ReactiveMongoOperations reactiveMongoTemplate;

    @Autowired
    public UrlServiceImpl(ReactiveMongoOperations reactiveMongoTemplate) {
        this.reactiveMongoTemplate = reactiveMongoTemplate;
    }

    /**
     * Shortens URL
     * @param urlFull
     * @return
     */
    @Override
    public Mono<Url> shortenUrl(String urlFull) {
        final String urlShort = getUrlShort(urlFull);
        final Url url = new Url(urlFull, urlShort, LocalDate.now());
        return reactiveMongoTemplate.save(url)
                .map(u -> {
                    u.setUrlShort(URL_PREFIX + u.getUrlShort());
                    return u;
                });
    }

    private static String getUrlShort(String urlFull) {
        return DigestUtils.md5DigestAsHex(urlFull.getBytes()).substring(0, SHORT_URL_LENGTH);
    }

    /**
     * Get full URL by short form
     * @param shortUrl
     * @return
     */
    @Override
    public Mono<Url> getFullUrl(String shortUrl) {
        return reactiveMongoTemplate.findById(shortUrl.replaceFirst(URL_PREFIX, ""), Url.class);
    }

    /**
     * Redirect to full URL by short form
     * @param shortFull
     * @return
     */
    @Override
    public Mono<Url> redirectToFullUrl(String shortFull) {
        return null;
    }
}
