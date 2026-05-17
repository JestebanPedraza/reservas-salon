package com.nelumbo.reservas.controller;

import com.nelumbo.reservas.dto.request.SucursalRequest;
import com.nelumbo.reservas.dto.response.SucursalResponse;
import com.nelumbo.reservas.service.interfaces.ISucursalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sucursales")
@RequiredArgsConstructor
public class SucursalController {

    private final ISucursalService sucursalService;

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<SucursalResponse> create(@Valid @RequestBody SucursalRequest request) {
        return new ResponseEntity<>(sucursalService.create(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<SucursalResponse> update(@PathVariable Integer id, @Valid @RequestBody SucursalRequest request) {
        return ResponseEntity.ok(sucursalService.update(id, request));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'GESTOR')")
    public ResponseEntity<SucursalResponse> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(sucursalService.findById(id));
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'GESTOR')")
    public ResponseEntity<List<SucursalResponse>> findAll() {
        return ResponseEntity.ok(sucursalService.findAll());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        sucursalService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
