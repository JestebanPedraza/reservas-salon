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

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/salones")
@RequiredArgsConstructor
public class SalonController {

    private final ISalonService salonService;

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<SalonResponse> create(@Valid @RequestBody SalonRequest request) {
        return new ResponseEntity<>(salonService.create(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<SalonResponse> update(@PathVariable Integer id, @Valid @RequestBody SalonRequest request) {
        return ResponseEntity.ok(salonService.update(id, request));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'GESTOR')")
    public ResponseEntity<SalonResponse> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(salonService.findById(id));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<SalonResponse>> findAll() {
        return ResponseEntity.ok(salonService.findAll());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        salonService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/sucursal/{sucursalId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'GESTOR')")
    public ResponseEntity<List<SalonResponse>> findBySucursal(@PathVariable Integer sucursalId) {
        return ResponseEntity.ok(salonService.findBySucursal(sucursalId));
    }
}

