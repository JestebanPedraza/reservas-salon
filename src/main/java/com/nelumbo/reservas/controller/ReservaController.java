package com.nelumbo.reservas.controller;

import com.nelumbo.reservas.dto.request.ReservaRequest;
import com.nelumbo.reservas.dto.response.FinalizarReservaResponse;
import com.nelumbo.reservas.dto.response.ReservaResponse;
import com.nelumbo.reservas.service.interfaces.IReservaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.nelumbo.reservas.dto.request.AprobarReservaRequest;
import com.nelumbo.reservas.dto.request.FinalizarReservaRequest;


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

    @PostMapping("/{reservaId}/aprobar")
    public String aprobarReserva(@PathVariable Integer reservaId, @RequestBody AprobarReservaRequest request) {
        //TODO: process POST request
        
        return request.toString();
    }

    @PostMapping("/finalizar")
    public ResponseEntity<FinalizarReservaResponse> finalizarReserva(@RequestBody FinalizarReservaRequest request) {
        return ResponseEntity.ok(reservaService.finalizarReserva(request.getDocumentoCliente(), request.getSalonId()));
    }
    


}
