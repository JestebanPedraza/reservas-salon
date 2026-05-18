package com.nelumbo.reservas.service.impl;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.nelumbo.reservas.dto.request.SalonRequest;
import com.nelumbo.reservas.dto.response.SalonResponse;
import com.nelumbo.reservas.entity.Salon;
import com.nelumbo.reservas.entity.Sucursal;
import com.nelumbo.reservas.exception.BadRequestException;
import com.nelumbo.reservas.repository.SalonRepository;
import com.nelumbo.reservas.repository.SucursalRepository;

@ExtendWith(MockitoExtension.class)
class SalonServiceImplTest {

    @Mock
    private SalonRepository salonRepository;
    @Mock
    private SucursalRepository sucursalRepository;

    @InjectMocks
    private SalonServiceImpl salonService;

    private Sucursal sucursal;
    private Salon salon;
    private SalonRequest request;

    @BeforeEach
    void setUp() {
        sucursal = new Sucursal();
        sucursal.setId(1);
        sucursal.setNombre("Sucursal A");

        request = new SalonRequest();
        request.setNombre("Gran Salón");
        request.setCapacidadMaxima(100);
        request.setCostoHora(new BigDecimal("150000"));
        request.setSucursalId(1);

        salon = Salon.builder()
                .id(1)
                .nombre("Gran Salón")
                .sucursal(sucursal)
                .capacidadMaxima(100)
                .costoHora(new BigDecimal("150000"))
                .build();
    }

    @Test
    void create_Exitoso() {
        when(sucursalRepository.findById(1)).thenReturn(Optional.of(sucursal));
        when(salonRepository.save(any(Salon.class))).thenReturn(salon);

        SalonResponse response = salonService.create(request);

        assertNotNull(response);
        assertEquals("Gran Salón", response.getNombre());
        verify(salonRepository).save(any(Salon.class));
    }

    @Test
    void update_NotFound() {
        when(salonRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(BadRequestException.class, () -> salonService.update(1, request));
    }

    @Test
    void findById_Exitoso() {
        when(salonRepository.findById(1)).thenReturn(Optional.of(salon));

        SalonResponse response = salonService.findById(1);

        assertNotNull(response);
        assertEquals(1, response.getId());
    }

    @Test
    void delete_Exitoso() {
        when(salonRepository.existsById(1)).thenReturn(true);
        
        salonService.delete(1);

        verify(salonRepository).deleteById(1);
    }
}
