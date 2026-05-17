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


@RestController
@RequestMapping("/reservas")
@RequiredArgsConstructor
public class ReservaController {

    private final IReservaService reservaService;

    @PostMapping
    @PreAuthorize("hasAuthority('GESTOR')")
    public ResponseEntity<ReservaResponse> registrar(@Valid @RequestBody ReservaRequest request) {
        return new ResponseEntity<>(reservaService.registrarReserva(request), HttpStatus.CREATED);
    }

    @GetMapping("/activas/{salonId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'GESTOR')")
    public ResponseEntity<List<ReservaResponse>> listarActivas(@PathVariable Integer salonId) {
        return ResponseEntity.ok(reservaService.listarReservasActivas(salonId));
    }

    @GetMapping("/buscar")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'GESTOR')")
    public ResponseEntity<List<ReservaResponse>> buscarPorDocumento(@RequestParam String documento) {
        return ResponseEntity.ok(reservaService.buscarPorDocumento(documento));
    }

    @PostMapping("/{id}/aprobar")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<AccionReservaResponse> aprobar(@PathVariable Integer id) {
        return ResponseEntity.ok(reservaService.aprobarReserva(id));
    }

    @PostMapping("/{id}/rechazar")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<AccionReservaResponse> rechazar(@PathVariable Integer id, @Valid @RequestBody RechazarReservaRequest request) {
        return ResponseEntity.ok(reservaService.rechazarReserva(id, request));
    }

    @PostMapping("/finalizar")
    @PreAuthorize("hasAuthority('GESTOR')")
    public ResponseEntity<FinalizarReservaResponse> finalizarReserva(@Valid @RequestBody FinalizarReservaRequest request) {
        return ResponseEntity.ok(reservaService.finalizarReserva(request.getDocumentoCliente(), request.getSalonId()));
    }

}
