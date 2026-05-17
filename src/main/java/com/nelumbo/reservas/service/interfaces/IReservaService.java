package com.nelumbo.reservas.service.interfaces;

import java.util.List;

import com.nelumbo.reservas.dto.request.RechazarReservaRequest;
import com.nelumbo.reservas.dto.request.ReservaRequest;
import com.nelumbo.reservas.dto.response.AccionReservaResponse;
import com.nelumbo.reservas.dto.response.FinalizarReservaResponse;
import com.nelumbo.reservas.dto.response.ReservaResponse;

public interface IReservaService {
    ReservaResponse registrarReserva(ReservaRequest request);
    FinalizarReservaResponse finalizarReserva(String documentoCliente, Integer salonId);
    List<ReservaResponse> listarReservasActivas(Integer salonId);
    List<ReservaResponse> buscarPorDocumento(String documento);
    AccionReservaResponse aprobarReserva(Integer id);
    AccionReservaResponse rechazarReserva(Integer id, RechazarReservaRequest request);
    void expirarReservasPendientes();
}
