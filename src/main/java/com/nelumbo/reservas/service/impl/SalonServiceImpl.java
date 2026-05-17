package com.nelumbo.reservas.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nelumbo.reservas.dto.request.SalonRequest;
import com.nelumbo.reservas.dto.response.SalonResponse;
import com.nelumbo.reservas.entity.Salon;
import com.nelumbo.reservas.entity.Sucursal;
import com.nelumbo.reservas.exception.BadRequestException;
import com.nelumbo.reservas.repository.SalonRepository;
import com.nelumbo.reservas.repository.SucursalRepository;
import com.nelumbo.reservas.service.interfaces.ISalonService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SalonServiceImpl implements ISalonService {

    private final SalonRepository salonRepository;
    private final SucursalRepository sucursalRepository;

    @Override
    @Transactional
    public SalonResponse create(SalonRequest request) {
        Sucursal sucursal = sucursalRepository.findById(request.getSucursalId())
                .orElseThrow(() -> new BadRequestException("Sucursal no encontrada"));

        Salon salon = Salon.builder()
                .nombre(request.getNombre())
                .sucursal(sucursal)
                .capacidadMaxima(request.getCapacidadMaxima())
                .costoHora(request.getCostoHora())
                .build();

        salon = salonRepository.save(salon);
        return mapToResponse(salon);
    }

    @Override
    @Transactional
    public SalonResponse update(Integer id, SalonRequest request) {
        Salon salon = salonRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Salón no encontrado"));

        Sucursal sucursal = sucursalRepository.findById(request.getSucursalId())
                .orElseThrow(() -> new BadRequestException("Sucursal no encontrada"));

        salon.setNombre(request.getNombre());
        salon.setSucursal(sucursal);
        salon.setCapacidadMaxima(request.getCapacidadMaxima());
        salon.setCostoHora(request.getCostoHora());

        return mapToResponse(salon);
    }

    @Override
    @Transactional(readOnly = true)
    public SalonResponse findById(Integer id) {
        return salonRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new BadRequestException("Salón no encontrado"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<SalonResponse> findAll() {
        return salonRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        if (!salonRepository.existsById(id)) {
            throw new BadRequestException("Salón no encontrado");
        }
        salonRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SalonResponse> findBySucursal(Integer sucursalId) {
        return salonRepository.findBySucursalId(sucursalId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private SalonResponse mapToResponse(Salon salon) {
        return SalonResponse.builder()
                .id(salon.getId())
                .nombre(salon.getNombre())
                .sucursal(salon.getSucursal().getNombre())
                .capacidadMaxima(salon.getCapacidadMaxima())
                .costoHora(salon.getCostoHora())
                .build();
    }
}

