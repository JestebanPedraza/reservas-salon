package com.nelumbo.reservas.service.interfaces;

import com.nelumbo.reservas.dto.request.ReservaRequest;
import com.nelumbo.reservas.dto.response.ReservaResponse;

import java.util.List;

import com.nelumbo.reservas.dto.response.FinalizarReservaResponse;

public interface IReservaService {
    ReservaResponse registrarReserva(ReservaRequest request);
    FinalizarReservaResponse finalizarReserva(String documentoCliente, Integer salonId);
    List<ReservaResponse> listarReservasActivas(Integer salonId);
    List<ReservaResponse> buscarPorDocumento(String documento);
    void aprobarReserva(Integer id);
    void rechazarReserva(Integer id, String motivo);
}
