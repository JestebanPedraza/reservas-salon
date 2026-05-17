-- Rename table sucursal to sucursales
ALTER TABLE sucursal RENAME TO sucursales;

-- Add foreign key gestor_id to sucursales table
ALTER TABLE sucursales ADD COLUMN gestor_id INTEGER NOT NULL;
ALTER TABLE sucursales ADD CONSTRAINT fk_sucursales_gestor_id FOREIGN KEY (gestor_id) REFERENCES usuarios(id);

-- Drop foreign key gestor_id from salones table
ALTER TABLE salones DROP CONSTRAINT fk_salones_gestor;
ALTER TABLE salones DROP COLUMN gestor_id;
