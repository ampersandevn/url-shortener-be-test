package com.github.vivyteam.controller;

import com.github.vivyteam.model.Url;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UrlControllerTest {
    private static final String LONG_URL = "https://google.com?q=somelongurl";
    private static final String SHORT_URL = "https://myservicedomain.de/fce6c241";
    @Autowired
    private WebTestClient webTestClient;

    @Test
    @WithMockUser(roles = "USER")
    public void shortenUrl() {
        webTestClient.post()
                .uri("/url/shorten")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(LONG_URL), String.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.urlShort").isEqualTo(SHORT_URL);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void getFullUrl() {
        shorten();

        webTestClient.get()
                .uri("/url/get/{shortUrl}", SHORT_URL)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Url.class);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void redirectFullUrl() {
        shorten();

        webTestClient.get()
                .uri("/url/redirect/{shortUrl}", SHORT_URL)
                .exchange()
                .expectStatus().isTemporaryRedirect()
                .expectHeader().valueEquals(HttpHeaders.LOCATION, LONG_URL);
    }

    private void shorten() {
        webTestClient.post()
                .uri("/url/shorten")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(LONG_URL), String.class)
                .exchange()
                .expectStatus().isOk();
    }
}
