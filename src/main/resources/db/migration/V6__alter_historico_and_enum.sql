ALTER TYPE estado_reserva ADD VALUE 'FINALIZADA';

ALTER TABLE historico_reservas ADD COLUMN nombre_cliente VARCHAR(100) NOT NULL;
ALTER TABLE historico_reservas ADD COLUMN salon_id INTEGER NOT NULL;


ALTER TABLE historico_reservas ADD CONSTRAINT fk_historico_salon FOREIGN KEY (salon_id) REFERENCES salones(id);
