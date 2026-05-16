-- Insertar Roles
INSERT INTO roles (nombre) VALUES ('ADMIN');
INSERT INTO roles (nombre) VALUES ('GESTOR');

-- Insertar Usuario Administrador
INSERT INTO usuarios (nombre, email, password, role_id) 
VALUES ('Administrador Sistema', 'admin@mail.com', '$2a$10$eeMB3FVSM2Oen8RIgXMwCud3lFL8sfcCedDTksqFr9pHhRzQ7p2ne', (SELECT id FROM roles WHERE nombre = 'ADMIN'));
