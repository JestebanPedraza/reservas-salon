package com.nelumbo.reservas.service.impl;

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

import com.nelumbo.reservas.dto.request.SucursalRequest;
import com.nelumbo.reservas.dto.response.SucursalResponse;
import com.nelumbo.reservas.entity.Role;
import com.nelumbo.reservas.entity.Sucursal;
import com.nelumbo.reservas.entity.User;
import com.nelumbo.reservas.enums.RoleName;
import com.nelumbo.reservas.exception.BadRequestException;
import com.nelumbo.reservas.repository.SucursalRepository;
import com.nelumbo.reservas.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class SucursalServiceImplTest {

    @Mock
    private SucursalRepository sucursalRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SucursalServiceImpl sucursalService;

    private User gestor;
    private Sucursal sucursal;
    private SucursalRequest request;

    @BeforeEach
    void setUp() {
        Role roleGestor = new Role();
        roleGestor.setNombre(RoleName.GESTOR.name());

        gestor = new User();
        gestor.setId(1);
        gestor.setNombre("Gestor Test");
        gestor.setEmail("gestor@test.com");
        gestor.setRole(roleGestor);

        request = new SucursalRequest();
        request.setNombre("Sucursal Norte");
        request.setDireccion("Calle 123");
        request.setGestorId(1);

        sucursal = Sucursal.builder()
                .id(1)
                .nombre("Sucursal Norte")
                .direccion("Calle 123")
                .gestor(gestor)
                .build();
    }

    @Test
    void create_Exitoso() {
        when(userRepository.findById(1)).thenReturn(Optional.of(gestor));
        when(sucursalRepository.save(any(Sucursal.class))).thenReturn(sucursal);

        SucursalResponse response = sucursalService.create(request);

        assertNotNull(response);
        assertEquals("Sucursal Norte", response.getNombre());
        assertEquals("Gestor Test", response.getNombreGestor());
        verify(sucursalRepository).save(any(Sucursal.class));
    }

    @Test
    void create_Error_NoEsGestor() {
        gestor.getRole().setNombre("ADMIN");
        when(userRepository.findById(1)).thenReturn(Optional.of(gestor));

        BadRequestException ex = assertThrows(BadRequestException.class, () -> sucursalService.create(request));
        assertEquals("El usuario asignado debe tener el rol GESTOR", ex.getMessage());
    }

    @Test
    void findById_Exitoso() {
        when(sucursalRepository.findById(1)).thenReturn(Optional.of(sucursal));

        SucursalResponse response = sucursalService.findById(1);

        assertNotNull(response);
        assertEquals(1, response.getId());
    }

    @Test
    void findById_NotFound() {
        when(sucursalRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(BadRequestException.class, () -> sucursalService.findById(1));
    }

    @Test
    void delete_Exitoso() {
        when(sucursalRepository.existsById(1)).thenReturn(true);
        
        sucursalService.delete(1);

        verify(sucursalRepository).deleteById(1);
    }
}
