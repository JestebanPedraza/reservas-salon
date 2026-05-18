package com.nelumbo.reservas.service.interfaces;

import java.util.List;

import com.nelumbo.reservas.dto.response.TopSucursalesFacturacionResponse;
import com.nelumbo.reservas.dto.response.indicadores.GananciasResponse;
import com.nelumbo.reservas.dto.response.indicadores.TopClientes;
import com.nelumbo.reservas.entity.Reserva;
import com.nelumbo.reservas.enums.PeriodoTipo;

public interface IIndicadoresService {
    List<TopClientes> getTop10ClientesGeneral();
    List<TopClientes> getTop10ClientesPorSalon(Integer salonId);
    List<Reserva> getClientesPrimerizos();
    GananciasResponse getGananciasPorSalon(Integer salonId, PeriodoTipo periodo);
    List<TopSucursalesFacturacionResponse> getTop3SucursalesMesActual();
}
