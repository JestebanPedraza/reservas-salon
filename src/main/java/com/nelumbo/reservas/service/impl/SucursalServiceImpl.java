package com.nelumbo.reservas.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nelumbo.reservas.dto.request.SucursalRequest;
import com.nelumbo.reservas.dto.response.SucursalResponse;
import com.nelumbo.reservas.entity.Sucursal;
import com.nelumbo.reservas.entity.User;
import com.nelumbo.reservas.entity.enums.RoleName;
import com.nelumbo.reservas.exception.BadRequestException;
import com.nelumbo.reservas.repository.SucursalRepository;
import com.nelumbo.reservas.repository.UserRepository;
import com.nelumbo.reservas.service.interfaces.ISucursalService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SucursalServiceImpl implements ISucursalService {

    private final SucursalRepository sucursalRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public SucursalResponse create(SucursalRequest request) {
        User gestor = userRepository.findById(request.getGestorId())
                .orElseThrow(() -> new BadRequestException("Gestor no encontrado"));

        if (!gestor.getRole().getNombre().equals(RoleName.GESTOR.name())) {
            throw new BadRequestException("El usuario asignado debe tener el rol GESTOR");
        }

        Sucursal sucursal = Sucursal.builder()
                .nombre(request.getNombre())
                .direccion(request.getDireccion())
                .gestor(gestor)
                .build();

        sucursal = sucursalRepository.save(sucursal);
        return mapToResponse(sucursal);
    }

    @Override
    @Transactional
    public SucursalResponse update(Integer id, SucursalRequest request) {
        Sucursal sucursal = sucursalRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Sucursal no encontrada"));

        User gestor = userRepository.findById(request.getGestorId())
                .orElseThrow(() -> new BadRequestException("Gestor no encontrado"));

        if (!gestor.getRole().getNombre().equals(RoleName.GESTOR.name())) {
            throw new BadRequestException("El usuario asignado debe tener el rol GESTOR");
        }

        sucursal.setNombre(request.getNombre());
        sucursal.setDireccion(request.getDireccion());
        sucursal.setGestor(gestor);

        return mapToResponse(sucursal);
    }

    @Override
    @Transactional(readOnly = true)
    public SucursalResponse findById(Integer id) {
        return sucursalRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new BadRequestException("Sucursal no encontrada"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<SucursalResponse> findAll() {
        return sucursalRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        if (!sucursalRepository.existsById(id)) {
            throw new BadRequestException("Sucursal no encontrada");
        }
        sucursalRepository.deleteById(id);
    }

    private SucursalResponse mapToResponse(Sucursal sucursal) {
        return SucursalResponse.builder()
                .id(sucursal.getId())
                .nombre(sucursal.getNombre())
                .direccion(sucursal.getDireccion())
                .nombreGestor(sucursal.getGestor().getNombre())
                .emailGestor(sucursal.getGestor().getEmail())
                .build();
    }
}
