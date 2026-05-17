package com.nelumbo.reservas.service.impl;

import com.nelumbo.reservas.entity.HistoricoReserva;
import com.nelumbo.reservas.entity.Reserva;
import com.nelumbo.reservas.repository.HistoricoReservaRepository;
import com.nelumbo.reservas.service.interfaces.IHistoricoReservaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class HistoricoReservaServiceImpl implements IHistoricoReservaService {

    private final HistoricoReservaRepository historicoReservaRepository;

    @Override
    @Transactional
    public void guardarHistorico(Reserva reserva, BigDecimal totalCobrado, LocalDateTime fechaFinReal) {
        HistoricoReserva historico = HistoricoReserva.builder()
                .reserva(reserva)
                .documentoCliente(reserva.getDocumentoCliente())
                .nombreCliente(reserva.getNombreCliente())
                .salon(reserva.getSalon())
                .fechaInicio(reserva.getFechaInicio())
                .fechaFinReal(fechaFinReal)
                .totalCobrado(totalCobrado)
                .build();
        
        historicoReservaRepository.save(historico);
    }
}
