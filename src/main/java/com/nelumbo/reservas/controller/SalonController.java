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

import com.nelumbo.reservas.dto.request.SalonRequest;
import com.nelumbo.reservas.dto.response.SalonResponse;
import com.nelumbo.reservas.service.interfaces.ISalonService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/salones")
@RequiredArgsConstructor
@Tag(name = "Salones", description = "Endpoints para la gestión de salones dentro de las sucursales")
public class SalonController {

    private final ISalonService salonService;

    @Operation(summary = "Crear salón", description = "Registra un nuevo salón (Solo ADMIN)")
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<SalonResponse> create(@Valid @RequestBody SalonRequest request) {
        return new ResponseEntity<>(salonService.create(request), HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar salón", description = "Modifica los datos de un salón existente (Solo ADMIN)")
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<SalonResponse> update(@PathVariable Integer id, @Valid @RequestBody SalonRequest request) {
        return ResponseEntity.ok(salonService.update(id, request));
    }

    @Operation(summary = "Obtener salón por ID", description = "Busca la información detallada de un salón")
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'GESTOR')")
    public ResponseEntity<SalonResponse> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(salonService.findById(id));
    }

    @Operation(summary = "Listar todos los salones", description = "Retorna una lista de todos los salones registrados")
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<SalonResponse>> findAll() {
        return ResponseEntity.ok(salonService.findAll());
    }

    @Operation(summary = "Eliminar salón", description = "Elimina físicamente un salón del sistema (Solo ADMIN)")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        salonService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Listar salones por sucursal", description = "Retorna los salones pertenecientes a una sucursal específica")
    @GetMapping("/sucursal/{sucursalId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'GESTOR')")
    public ResponseEntity<List<SalonResponse>> findBySucursal(@PathVariable Integer sucursalId) {
        return ResponseEntity.ok(salonService.findBySucursal(sucursalId));
    }
}

