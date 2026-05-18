package com.nelumbo.reservas.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nelumbo.reservas.dto.request.SucursalRequest;
import com.nelumbo.reservas.dto.response.SucursalResponse;
import com.nelumbo.reservas.service.interfaces.ISucursalService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/sucursales")
@RequiredArgsConstructor
@Tag(name = "Sucursales", description = "Endpoints para la gestión de sucursales físicas")
public class SucursalController {

    private final ISucursalService sucursalService;

    @Operation(summary = "Crear sucursal", description = "Registra una nueva sucursal física (Solo ADMIN)")
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<SucursalResponse> create(@Valid @RequestBody SucursalRequest request) {
        return new ResponseEntity<>(sucursalService.create(request), HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar sucursal", description = "Modifica los datos de una sucursal existente (Solo ADMIN)")
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<SucursalResponse> update(@PathVariable Integer id, @Valid @RequestBody SucursalRequest request) {
        return ResponseEntity.ok(sucursalService.update(id, request));
    }

    @Operation(summary = "Obtener sucursal por ID", description = "Busca la información detallada de una sucursal")
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'GESTOR')")
    public ResponseEntity<SucursalResponse> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(sucursalService.findById(id));
    }

    @Operation(summary = "Listar todas las sucursales", description = "Retorna una lista de todas las sucursales registradas")
    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'GESTOR')")
    public ResponseEntity<List<SucursalResponse>> findAll() {
        return ResponseEntity.ok(sucursalService.findAll());
    }

    @Operation(summary = "Eliminar sucursal", description = "Elimina físicamente una sucursal del sistema (Solo ADMIN)")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        sucursalService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
