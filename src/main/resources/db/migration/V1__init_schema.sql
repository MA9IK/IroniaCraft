CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    telegram_chat_id BIGINT UNIQUE NOT NULL,
    username VARCHAR(255),
    role VARCHAR(255) NOT NULL DEFAULT 'ROLE_USER',
    created_at timestamptz NOT NULL DEFAULT now()
);

CREATE TABLE servers (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    modpack_path VARCHAR(255),
    minecraft_version VARCHAR(16),
    java_version int DEFAULT 21,
    port integer NOT NULL,
    ram_mb integer default 4096,
    status VARCHAR(50) default 'STOPPED',
    owner_id BIGINT REFERENCES users(id) NOT NULL,
    created_at timestamptz NOT NULL DEFAULT now()
);

CREATE INDEX idx_users_telegram_chat_id ON users(telegram_chat_id);
CREATE INDEX idx_servers_owner_id ON servers(owner_id);
CREATE INDEX idx_servers_status ON servers(status);