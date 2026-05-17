package com.nelumbo.reservas.service.interfaces;

import com.nelumbo.reservas.entity.Reserva;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface IHistoricoReservaService {
    void guardarHistorico(Reserva reserva, BigDecimal totalCobrado, LocalDateTime fechaFinReal);
}
