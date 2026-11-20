package com.learning.url_shortener.service;

import java.net.URI;
import java.time.Instant;

import com.learning.url_shortener.repository.UrlIdGenerator;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Service;

import com.learning.url_shortener.dto.CreateUrlResponse;
import com.learning.url_shortener.entity.UrlMapping;
import com.learning.url_shortener.repository.UrlRepository;
import com.learning.url_shortener.util.Base62Encoder;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UrlService {

    private static final String DOMAIN =
            "https://short.ly/";
    private final UrlIdGenerator idGenerator;
    private final EntityManager entityManager;
    private final UrlRepository repository;

    public UrlService(UrlIdGenerator idGenerator, EntityManager entityManager, UrlRepository repository) {
        this.idGenerator = idGenerator;
        this.entityManager = entityManager;
        this.repository = repository;
    }

    @Transactional
    public CreateUrlResponse createShortUrl(String originalUrl) {

        validateUrl(originalUrl);
        Long id = idGenerator.generateId();
        String code = Base62Encoder.encode(id);
        UrlMapping mapping = new UrlMapping();
        mapping.setId(id);
        mapping.setShortCode(code);
        mapping.setOriginalUrl(originalUrl);
        mapping.setCreatedAt(Instant.now());
        mapping.setClickCount(0L);

        // 5. Persist complete entity
        entityManager.persist(mapping);

        return new CreateUrlResponse(
                code,
                DOMAIN + code
        );
    }

    @Transactional(readOnly = true)
    public String getOriginalUrl(String code) {

        UrlMapping mapping = repository
                .findByShortCode(code)
                .orElseThrow(() ->
                        new RuntimeException("URL not found"));

        if (mapping.getExpiresAt() != null &&
                mapping.getExpiresAt().isBefore(Instant.now())) {
            throw new RuntimeException("URL expired");
        }

        return mapping.getOriginalUrl();
    }

    private void validateUrl(String url) {
        if (url == null || url.isBlank()) {
            throw new IllegalArgumentException("URL cannot be empty");
        }
        URI uri;

        try {
            uri = URI.create(url);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException(
                    "Invalid URL", ex
            );
        }
        if (!"http".equalsIgnoreCase(uri.getScheme())
                && !"https".equalsIgnoreCase(uri.getScheme())
                || uri.getHost() == null) {

            throw new IllegalArgumentException(
                    "Only valid HTTP/HTTPS URLs are allowed"
            );
        }
    }
}
