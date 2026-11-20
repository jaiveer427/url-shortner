package com.learning.url_shortener.repository.impl;

import com.learning.url_shortener.repository.UrlIdGenerator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class PostgresSequenceUrlIdGenerator
        implements UrlIdGenerator {

    private final JdbcTemplate jdbcTemplate;

    public PostgresSequenceUrlIdGenerator(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long generateId() {

        return jdbcTemplate.queryForObject(
                "SELECT nextval('url_id_seq')",
                Long.class
        );
    }
}