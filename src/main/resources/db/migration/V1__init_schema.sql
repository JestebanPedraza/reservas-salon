CREATE TYPE estado_reserva AS ENUM (
    'PENDIENTE_APROBACION', 
    'ACTIVA', 
    'RECHAZADA', 
    'EXPIRADA'
);

CREATE TABLE roles (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE usuarios (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(200) NOT NULL,
    role_id INTEGER NOT NULL,
    CONSTRAINT fk_usuarios_roles FOREIGN KEY (role_id) REFERENCES roles(id)
);

CREATE TABLE sucursal (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    direccion VARCHAR(150) NOT NULL
);

CREATE TABLE salones (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    capacidad_maxima INTEGER NOT NULL,
    costo_hora INTEGER NOT NULL,
    sucursal_id INTEGER NOT NULL,
    gestor_id INTEGER NOT NULL,
    CONSTRAINT fk_salones_sucursal FOREIGN KEY (sucursal_id) REFERENCES sucursal(id),
    CONSTRAINT fk_salones_gestor FOREIGN KEY (gestor_id) REFERENCES usuarios(id)
);

CREATE TABLE reservas (
    id SERIAL PRIMARY KEY,
    nombre_cliente VARCHAR(100) NOT NULL,
    documento_cliente VARCHAR(12) NOT NULL,
    fecha_inicio TIMESTAMP NOT NULL,
    fecha_fin_estimada TIMESTAMP NOT NULL,
    total_estimado NUMERIC(12,2) NOT NULL,
    asistentes INTEGER NOT NULL,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    estado estado_reserva NOT NULL DEFAULT 'ACTIVA',
    motivo_rechazo VARCHAR(200),
    salon_id INTEGER NOT NULL,
    CONSTRAINT fk_reservas_salon FOREIGN KEY (salon_id) REFERENCES salones(id)
);

CREATE TABLE historico_reservas (
    id SERIAL PRIMARY KEY,
    documento_cliente VARCHAR(12) NOT NULL,
    fecha_inicio TIMESTAMP NOT NULL,
    fecha_fin_real TIMESTAMP NOT NULL,
    total_cobrado NUMERIC(12,2) NOT NULL,
    reserva_id INTEGER NOT NULL,
    CONSTRAINT fk_historico_reserva_original FOREIGN KEY (reserva_id) REFERENCES reservas(id)
);
