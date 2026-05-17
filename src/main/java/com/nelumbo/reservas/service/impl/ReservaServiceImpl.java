package com.nelumbo.reservas.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.nelumbo.reservas.dto.request.ReservaRequest;
import com.nelumbo.reservas.dto.response.ReservaResponse;
import com.nelumbo.reservas.entity.enums.EstadoReserva;
import com.nelumbo.reservas.entity.Reserva;
import com.nelumbo.reservas.entity.Salon;
import com.nelumbo.reservas.exception.BadRequestException;
import com.nelumbo.reservas.repository.ReservaRepository;
import com.nelumbo.reservas.service.interfaces.ISalonService;
import com.nelumbo.reservas.service.interfaces.IReservaService;
import com.nelumbo.reservas.service.interfaces.IHistoricoReservaService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.nelumbo.reservas.dto.response.FinalizarReservaResponse;

@Service
@RequiredArgsConstructor
public class ReservaServiceImpl implements IReservaService {

    private final ReservaRepository reservaRepository;
    private final ISalonService salonService;
    private final IHistoricoReservaService historicoReservaService;

    private static final BigDecimal LIMITE_PREMIUM = new BigDecimal("500000.0");

    @Override
    @Transactional
    public ReservaResponse registrarReserva(ReservaRequest request) {

        if (!request.getFechaFinEstimada().isAfter(request.getFechaInicio())) {
            throw new BadRequestException("La fecha de fin estimada debe ser posterior a la de inicio");
        }

        Salon salon = salonService.findSalonById(request.getSalonId());

        if (reservaRepository.existsByDocumentoClienteAndEstado(request.getDocumentoCliente(), EstadoReserva.ACTIVA)) {
            throw new BadRequestException("No se puede Registrar Reserva, ya existe una reserva activa para este documento en este u otro salón");
        }

        Integer ocupados = reservaRepository.sumAsistentesBySalonInRange(
                request.getSalonId(), request.getFechaInicio(), request.getFechaFinEstimada(), EstadoReserva.ACTIVA);
        
        int totalAsistentes = (ocupados != null ? ocupados : 0) + request.getAsistentes();
        if (totalAsistentes > salon.getCapacidadMaxima()) {
            throw new BadRequestException("No se puede Registrar Reserva, capacidad insuficiente en el salón");
        }

        BigDecimal horas = BigDecimal.valueOf(Math.ceil(Duration.between(request.getFechaInicio(), request.getFechaFinEstimada()).toMinutes() / 60.0));
        BigDecimal totalEstimado = horas.multiply(salon.getCostoHora());

        EstadoReserva estadoInicial = (totalEstimado.compareTo(LIMITE_PREMIUM) > 0) ? EstadoReserva.PENDIENTE_APROBACION : EstadoReserva.ACTIVA;

        Reserva reserva = Reserva.builder()
                .documentoCliente(request.getDocumentoCliente())
                .nombreCliente(request.getNombreCliente())
                .salon(salon)
                .fechaInicio(request.getFechaInicio())
                .fechaFinEstimada(request.getFechaFinEstimada())
                .totalEstimado(totalEstimado)
                .asistentes(request.getAsistentes())
                .estado(estadoInicial)
                .fechaCreacion(LocalDateTime.now())
                .build();

        reserva = reservaRepository.save(reserva);
        return mapToResponse(reserva);
    }

    @Override
    @Transactional
    public FinalizarReservaResponse finalizarReserva(String documentoCliente, Integer salonId) {
        Reserva reserva = reservaRepository.findByDocumentoClienteAndSalonIdAndEstado(documentoCliente, salonId, EstadoReserva.ACTIVA)
                .orElseThrow(() -> new BadRequestException("No se encontró una reserva activa para el documento y salón especificados"));

        reserva.setEstado(EstadoReserva.FINALIZADA);
        LocalDateTime fechaFinReal = LocalDateTime.now();

        reservaRepository.save(reserva);

        long minutosEfectivos = Duration.between(reserva.getFechaInicio(), fechaFinReal).toMinutes();

        // BigDecimal horasEfectivas = BigDecimal.valueOf(Math.ceil(minutosEfectivos / 60.0)); -- PUEDE DAR PROBLEMAS DE PRECISIÓN
        BigDecimal horasEfectivas = BigDecimal.valueOf(minutosEfectivos)
                        .divide(BigDecimal.valueOf(60), 2, RoundingMode.CEILING);

        if (horasEfectivas.compareTo(BigDecimal.ONE) < 0) horasEfectivas = BigDecimal.ONE;
        
        BigDecimal totalCobrado = horasEfectivas.multiply(reserva.getSalon().getCostoHora());

        historicoReservaService.guardarHistorico(reserva, totalCobrado, fechaFinReal);

        return FinalizarReservaResponse.builder()
                .mensaje("Reserva finalizada")
                .totalCabrado(totalCobrado)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservaResponse> listarReservasActivas(Integer salonId) {
        return reservaRepository.findBySalonIdAndEstado(salonId, EstadoReserva.ACTIVA).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservaResponse> buscarPorDocumento(String documento) {
        return reservaRepository.findByDocumentoClienteContainingAndEstado(documento, EstadoReserva.ACTIVA).stream()
                .map(r -> mapToResponse(r))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void aprobarReserva(Integer id) {
        // Por implementar
    }

    @Override
    @Transactional
    public void rechazarReserva(Integer id, String motivo) {
        // Por implementar
    }

    private ReservaResponse mapToResponse(Reserva reserva) {
        return ReservaResponse.builder()
                .id(reserva.getId())
                .documentoCliente(reserva.getDocumentoCliente())
                .nombreCliente(reserva.getNombreCliente())
                .salonId(reserva.getSalon().getId())
                .salonNombre(reserva.getSalon().getNombre())
                .fechaInicio(reserva.getFechaInicio())
                .fechaFinEstimada(reserva.getFechaFinEstimada())
                .totalEstimado(reserva.getTotalEstimado())
                .asistentes(reserva.getAsistentes())
                .estado(reserva.getEstado().name())
                .build();
    }

    private FinalizarReservaResponse mapToFinalizarResponse(Reserva reserva) {
        return FinalizarReservaResponse.builder()
                .mensaje("Reserva finalizada exitosamente")
                .totalCabrado(reserva.getTotalEstimado())
                .build();
    }
}
