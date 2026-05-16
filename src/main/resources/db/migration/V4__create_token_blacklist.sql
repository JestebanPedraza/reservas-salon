CREATE TABLE token_blacklist (
    id SERIAL PRIMARY KEY,
    token VARCHAR(500) NOT NULL,
    expiry_date TIMESTAMP NOT NULL
);
    CONSTRAINT fk_usuarios_roles FOREIGN KEY (role_id) REFERENCES roles(id)
);