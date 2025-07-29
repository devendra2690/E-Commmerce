CREATE TABLE api_keys (
    id SERIAL PRIMARY KEY,
    client_name VARCHAR(100) UNIQUE NOT NULL,
    api_key UUID UNIQUE NOT NULL DEFAULT gen_random_uuid(),
    rate_limit INT NOT NULL DEFAULT 10, -- Requests per second
    is_active BOOLEAN DEFAULT TRUE
);