package com.learning.url_shortener.entity;

import java.time.Instant;

import jakarta.persistence.*;

@Entity
@Table(
        name = "url_mapping",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_url_mapping_short_code",
                        columnNames = "short_code"
                )
        }
)
public class UrlMapping {

    @Id
    private Long id;

    @Column(
            name = "short_code",
            nullable = false,
            length = 20
    )
    private String shortCode;

    @Column(
            name = "original_url",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String originalUrl;

    @Column(
            name = "created_at",
            nullable = false
    )
    private Instant createdAt;

    @Column(name = "expires_at")
    private Instant expiresAt;

    @Column(
            name = "click_count",
            nullable = false
    )
    private Long clickCount = 0L;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getShortCode() {
        return shortCode;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }

    public Long getClickCount() {
        return clickCount;
    }

    public void setClickCount(Long clickCount) {
        this.clickCount = clickCount;
    }


}