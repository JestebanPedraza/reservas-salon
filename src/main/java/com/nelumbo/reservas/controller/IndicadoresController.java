package com.nelumbo.reservas.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nelumbo.reservas.dto.response.ReservaResponse;
import com.nelumbo.reservas.dto.response.TopSucursalesFacturacionResponse;
import com.nelumbo.reservas.dto.response.indicadores.GananciasResponse;
import com.nelumbo.reservas.dto.response.indicadores.TopClientes;
import com.nelumbo.reservas.entity.Reserva;
import com.nelumbo.reservas.enums.PeriodoTipo;
import com.nelumbo.reservas.service.interfaces.IIndicadoresService;
import com.nelumbo.reservas.service.interfaces.IReservaService;

import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/indicadores")
@RequiredArgsConstructor
@Tag(name = "Indicadores", description = "Endpoints para la obtención de estadísticas y reportes")
public class IndicadoresController {
    private final IIndicadoresService indicadoresService;
    private final IReservaService reservaService;

    @Operation(summary = "Top 10 clientes general", description = "Retorna los 10 clientes con más reservas en todo el sistema")
    @GetMapping("/top-clientes-general")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'GESTOR')")
    public ResponseEntity<List<TopClientes>> getTop10Clientes() {
        return ResponseEntity.ok(indicadoresService.getTop10ClientesGeneral());
    }

    @Operation(summary = "Top 10 clientes por salón", description = "Retorna los 10 clientes con más reservas en un salón específico")
    @GetMapping("/top-clientes-salon/{salonId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'GESTOR')")
    public ResponseEntity<List<TopClientes>> getTop10ClientesPorSalon(@PathVariable Integer salonId) {
        return ResponseEntity.ok(indicadoresService.getTop10ClientesPorSalon(salonId));
    }

    @Operation(summary = "Clientes primerizos", description = "Lista las reservas de clientes que reservan por primera vez en un salón")
    @GetMapping("/clientes-primerizos")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'GESTOR')")
    public ResponseEntity<List<Reserva>> getClientesPrimerizos() {
        return ResponseEntity.ok(indicadoresService.getClientesPrimerizos());
    }

    @Operation(summary = "Ganancias por salón", description = "Retorna las ganancias de un salón en un periodo específico (HOY, SEMANA, MES, ANIO)")
    @GetMapping("/salones/{salonId}/ganancias")
    @PreAuthorize("hasAnyAuthority('GESTOR')")
    public ResponseEntity<GananciasResponse> getGanancias(
            @PathVariable Integer salonId,
            @RequestParam PeriodoTipo periodo) {
        return ResponseEntity.ok(indicadoresService.getGananciasPorSalon(salonId, periodo));
    }

    @Operation(summary = "Top 3 sucursales", description = "Retorna las 3 sucursales con mayor facturación en el mes actual")
    @GetMapping("/top-sucursales")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<TopSucursalesFacturacionResponse>> getTopSucursales() {
        return ResponseEntity.ok(indicadoresService.getTop3SucursalesMesActual());
    }

    @Operation(summary = "Buscar reservas por documento", description = "Busca reservas activas mediante coincidencia parcial del documento del cliente")
    @GetMapping("/buscar-documento")
    @PreAuthorize("hasAnyRole('ADMIN', 'GESTOR')")
    public ResponseEntity<List<ReservaResponse>> buscarPorDocumento(@RequestParam String query) {
        return ResponseEntity.ok(reservaService.buscarPorDocumento(query));
    }
}
