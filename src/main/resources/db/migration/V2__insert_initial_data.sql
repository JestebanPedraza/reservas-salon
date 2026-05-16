-- Insertar Roles
INSERT INTO roles (nombre) VALUES ('ADMIN');
INSERT INTO roles (nombre) VALUES ('GESTOR');

-- Insertar Usuario Administrador
INSERT INTO usuarios (nombre, email, password, role_id) 
VALUES ('Administrador Sistema', 'admin@mail.com', '$2a$10$Xm5I16PZ5y.VozpYV6j.puxVv.h9WUn6D3.u8Q7gI9g9qWn.fIu7i', (SELECT id FROM roles WHERE nombre = 'ADMIN'));
