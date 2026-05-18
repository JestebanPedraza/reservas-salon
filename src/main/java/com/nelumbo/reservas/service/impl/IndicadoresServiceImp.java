package com.nelumbo.reservas.service.impl;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nelumbo.reservas.dto.response.TopSucursalesFacturacionResponse;
import com.nelumbo.reservas.dto.response.indicadores.GananciasResponse;
import com.nelumbo.reservas.dto.response.indicadores.TopClientes;
import com.nelumbo.reservas.entity.Reserva;
import com.nelumbo.reservas.enums.PeriodoTipo;
import com.nelumbo.reservas.repository.HistoricoReservaRepository;
import com.nelumbo.reservas.repository.ReservaRepository;
import com.nelumbo.reservas.service.interfaces.IIndicadoresService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IndicadoresServiceImp implements IIndicadoresService {
    
    private final ReservaRepository reservaRepository;
    private final HistoricoReservaRepository historicoReservaRepository;

    @Override
    @Transactional(readOnly = true)
    public List<TopClientes> getTop10ClientesGeneral() {
        return reservaRepository.findTop10ClientesGeneral();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TopClientes> getTop10ClientesPorSalon(Integer salonId) {
        return reservaRepository.findTop10ClientesBySalon(salonId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Reserva> getClientesPrimerizos() {
        return reservaRepository.findClientesPrimerizos();
    }

    @Override
    @Transactional(readOnly = true)
    public GananciasResponse getGananciasPorSalon(Integer salonId, PeriodoTipo periodo) {
        LocalDateTime inicio = switch (periodo) {
            case HOY   -> LocalDate.now().atStartOfDay();
            case SEMANA -> LocalDate.now().with(DayOfWeek.MONDAY).atStartOfDay();
            case MES   -> LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()).atStartOfDay();
            case ANIO  -> LocalDate.now().with(TemporalAdjusters.firstDayOfYear()).atStartOfDay();
        };

        LocalDateTime fin = LocalDateTime.now();
        Double ganancias = historicoReservaRepository.sumGanancias(salonId, inicio, fin);

        return GananciasResponse.builder()
                .salonId(salonId)
                .periodo(periodo)
                .total(ganancias != null ? ganancias : 0.0)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TopSucursalesFacturacionResponse> getTop3SucursalesMesActual() {
        LocalDateTime inicioMes = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()).atStartOfDay();
        return historicoReservaRepository.findTopSucursales(inicioMes, PageRequest.of(0, 3));
    }

}
