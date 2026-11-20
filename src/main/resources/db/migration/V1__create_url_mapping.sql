CREATE SEQUENCE url_id_seq
    AS BIGINT
    START WITH 1
    INCREMENT BY 1
    CACHE 1000
    NO CYCLE;

CREATE TABLE url_mapping (
                             id BIGINT PRIMARY KEY,
                             short_code VARCHAR(20) NOT NULL,
                             original_url TEXT NOT NULL,
                             created_at TIMESTAMPTZ NOT NULL,
                             expires_at TIMESTAMPTZ,
                             click_count BIGINT NOT NULL DEFAULT 0,

                             CONSTRAINT uk_url_mapping_short_code
                                 UNIQUE (short_code),

                             CONSTRAINT chk_url_mapping_click_count
                                 CHECK (click_count >= 0)
);

ALTER SEQUENCE url_id_seq
    OWNED BY url_mapping.id;