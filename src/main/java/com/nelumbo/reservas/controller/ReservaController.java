package com.nelumbo.reservas.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nelumbo.reservas.dto.request.FinalizarReservaRequest;
import com.nelumbo.reservas.dto.request.RechazarReservaRequest;
import com.nelumbo.reservas.dto.request.ReservaRequest;
import com.nelumbo.reservas.dto.response.AccionReservaResponse;
import com.nelumbo.reservas.dto.response.FinalizarReservaResponse;
import com.nelumbo.reservas.dto.response.ReservaResponse;
import com.nelumbo.reservas.service.interfaces.IReservaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("/reservas")
@RequiredArgsConstructor
@Tag(name = "Reservas", description = "Endpoints para la gestión de reservas de salones")
public class ReservaController {

    private final IReservaService reservaService;

    @Operation(summary = "Registrar reserva", description = "Crea una nueva reserva. Si el total supera 500k, queda en PENDIENTE_APROBACION")
    @PostMapping
    @PreAuthorize("hasAuthority('GESTOR')")
    public ResponseEntity<ReservaResponse> registrar(@Valid @RequestBody ReservaRequest request) {
        return new ResponseEntity<>(reservaService.registrarReserva(request), HttpStatus.CREATED);
    }

    @Operation(summary = "Listar reservas activas", description = "Lista todas las reservas con estado ACTIVA para un salón")
    @GetMapping("/activas/{salonId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'GESTOR')")
    public ResponseEntity<List<ReservaResponse>> listarActivas(@PathVariable Integer salonId) {
        return ResponseEntity.ok(reservaService.listarReservasActivas(salonId));
    }

    @Operation(summary = "Buscar por documento", description = "Busca reservas activas por documento del cliente (coincidencia parcial)")
    @GetMapping("/buscar")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'GESTOR')")
    public ResponseEntity<List<ReservaResponse>> buscarPorDocumento(@RequestParam String documento) {
        return ResponseEntity.ok(reservaService.buscarPorDocumento(documento));
    }

    @Operation(summary = "Aprobar reserva", description = "Cambia el estado de una reserva PENDIENTE_APROBACION a ACTIVA (Solo ADMIN)")
    @PostMapping("/{id}/aprobar")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<AccionReservaResponse> aprobar(@PathVariable Integer id) {
        return ResponseEntity.ok(reservaService.aprobarReserva(id));
    }

    @Operation(summary = "Rechazar reserva", description = "Rechaza una reserva pendiente y notifica al gestor (Solo ADMIN)")
    @PostMapping("/{id}/rechazar")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<AccionReservaResponse> rechazar(@PathVariable Integer id, @Valid @RequestBody RechazarReservaRequest request) {
        return ResponseEntity.ok(reservaService.rechazarReserva(id, request));
    }

    @Operation(summary = "Finalizar reserva", description = "Termina una reserva activa, calcula el cobro final y guarda el histórico")
    @PostMapping("/finalizar")
    @PreAuthorize("hasAuthority('GESTOR')")
    public ResponseEntity<FinalizarReservaResponse> finalizarReserva(@Valid @RequestBody FinalizarReservaRequest request) {
        return ResponseEntity.ok(reservaService.finalizarReserva(request.getDocumentoCliente(), request.getSalonId()));
    }

}
