package com.nelumbo.reservas.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.nelumbo.reservas.dto.request.ReservaRequest;
import com.nelumbo.reservas.dto.response.ReservaResponse;
import com.nelumbo.reservas.entity.Reserva;
import com.nelumbo.reservas.entity.Salon;
import com.nelumbo.reservas.exception.BadRequestException;
import com.nelumbo.reservas.integration.notificaciones.NotificacionHelper;
import com.nelumbo.reservas.repository.ReservaRepository;
import com.nelumbo.reservas.service.interfaces.IHistoricoReservaService;
import com.nelumbo.reservas.service.interfaces.ISalonService;

@ExtendWith(MockitoExtension.class)
class ReservaServiceImplTest {

    @Mock
    private ReservaRepository reservaRepository;
    @Mock
    private ISalonService salonService;
    @Mock
    private IHistoricoReservaService historicoReservaService;
    @Mock
    private NotificacionHelper notificacionHelper;

    @InjectMocks
    private ReservaServiceImpl reservaService;

    private Salon salon;
    private ReservaRequest request;

    @BeforeEach
    void setUp() {
        salon = new Salon();
        salon.setId(1);
        salon.setNombre("Salón A");
        salon.setCapacidadMaxima(50);
        salon.setCostoHora(new BigDecimal("100000"));

        request = new ReservaRequest();
        request.setSalonId(1);
        request.setDocumentoCliente("12345678");
        request.setNombreCliente("Juan Perez");
        request.setAsistentes(10);
        request.setFechaInicio(LocalDateTime.now().plusDays(1));
        request.setFechaFinEstimada(LocalDateTime.now().plusDays(1).plusHours(2));
    }

    @Test
    void registrarReserva_Exitoso() {
        // Arrange
        when(salonService.findSalonById(anyInt())).thenReturn(salon);
        when(reservaRepository.existsByDocumentoClienteAndEstado(anyString(), any())).thenReturn(false);
        when(reservaRepository.sumAsistentesBySalonInRange(any(), any(), any(), any())).thenReturn(0);
        
        when(reservaRepository.save(any(Reserva.class))).thenAnswer(invocation -> {
            Reserva r = invocation.getArgument(0);
            r.setId(100);
            return r;
        });

        // Act
        ReservaResponse result = reservaService.registrarReserva(request);

        // Assert
        assertNotNull(result);
        assertEquals("ACTIVA", result.getEstado());
        assertEquals(101, result.getId() == 100 ? 101 : 1); // Solo para asegurar que se guardó
        verify(reservaRepository, times(1)).save(any(Reserva.class));
    }

    @Test
    void registrarReserva_ErrorFechas() {
        // Arrange
        request.setFechaFinEstimada(request.getFechaInicio().minusHours(1));

        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            reservaService.registrarReserva(request);
        });

        assertEquals("La fecha de fin estimada debe ser posterior a la de inicio", exception.getMessage());
    }

    @Test
    void registrarReserva_CapacidadExcedida() {
        // Arrange
        request.setAsistentes(60); // Salón tiene 50
        when(salonService.findSalonById(anyInt())).thenReturn(salon);
        when(reservaRepository.existsByDocumentoClienteAndEstado(anyString(), any())).thenReturn(false);
        when(reservaRepository.sumAsistentesBySalonInRange(any(), any(), any(), any())).thenReturn(0);

        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            reservaService.registrarReserva(request);
        });

        assertTrue(exception.getMessage().contains("capacidad insuficiente"));
    }

    @Test
    void registrarReserva_Premium_PendienteAprobacion() {
        // Arrange
        // 100k por hora * 6 horas = 600k (> 500k limite premium)
        request.setFechaFinEstimada(request.getFechaInicio().plusHours(6));
        
        when(salonService.findSalonById(anyInt())).thenReturn(salon);
        when(reservaRepository.existsByDocumentoClienteAndEstado(anyString(), any())).thenReturn(false);
        
        when(reservaRepository.save(any(Reserva.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        ReservaResponse result = reservaService.registrarReserva(request);

        // Assert
        assertEquals("PENDIENTE_APROBACION", result.getEstado());
    }
}
