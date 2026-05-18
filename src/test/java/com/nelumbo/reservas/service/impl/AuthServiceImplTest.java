package com.nelumbo.reservas.service.impl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.nelumbo.reservas.dto.request.LoginRequest;
import com.nelumbo.reservas.dto.request.RegisterRequest;
import com.nelumbo.reservas.dto.response.AuthResponse;
import com.nelumbo.reservas.dto.response.RegisterResponse;
import com.nelumbo.reservas.entity.Role;
import com.nelumbo.reservas.entity.User;
import com.nelumbo.reservas.repository.RoleRepository;
import com.nelumbo.reservas.repository.UserRepository;
import com.nelumbo.reservas.security.JwtService;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtService jwtUtil;
    @Mock
    private UserDetailsService userDetailsService;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void registerGestor_Exitoso() {
        // Arrange
        RegisterRequest request = new RegisterRequest();
        request.setEmail("test@email.com");
        request.setNombre("Test User");
        request.setPassword("password");

        Role role = new Role();
        role.setNombre("GESTOR");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(roleRepository.findByNombre("GESTOR")).thenReturn(role);
        when(passwordEncoder.encode(anyString())).thenReturn("hashed_password");

        // Act
        RegisterResponse response = authService.registerGestor(request);

        // Assert
        assertNotNull(response);
        assertEquals("test@email.com", response.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void registerGestor_EmailDuplicado() {
        // Arrange
        RegisterRequest request = new RegisterRequest();
        request.setEmail("existing@email.com");
        
        when(userRepository.findByEmail("existing@email.com")).thenReturn(Optional.of(new User()));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.registerGestor(request);
        });

        assertEquals("El email ya se encuentra registrado", exception.getMessage());
    }

    @Test
    void login_Exitoso() {
        // Arrange
        LoginRequest request = new LoginRequest("user@email.com", "pass");
        UserDetails userDetails = mock(UserDetails.class);

        when(userDetailsService.loadUserByUsername(anyString())).thenReturn(userDetails);
        when(jwtUtil.generateToken(any())).thenReturn("valid_token");

        // Act
        AuthResponse response = authService.login(request);

        // Assert
        assertNotNull(response);
        assertEquals("valid_token", response.getToken());
        verify(authenticationManager).authenticate(any());
    }
}
