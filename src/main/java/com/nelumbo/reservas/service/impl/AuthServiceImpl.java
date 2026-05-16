package com.nelumbo.reservas.service.impl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nelumbo.reservas.dto.request.LoginRequest;
import com.nelumbo.reservas.dto.request.RegisterRequest;
import com.nelumbo.reservas.dto.response.AuthResponse;
import com.nelumbo.reservas.dto.response.RegisterResponse;
import com.nelumbo.reservas.entity.Role;
import com.nelumbo.reservas.entity.User;
import com.nelumbo.reservas.repository.RoleRepository;
import com.nelumbo.reservas.repository.UserRepository;
import com.nelumbo.reservas.security.JwtService;
import com.nelumbo.reservas.service.interfaces.IAuthService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements IAuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtUtil;
    private final UserDetailsService userDetailsService;

    @Override
    @Transactional
    public RegisterResponse registerGestor(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("El email ya se encuentra registrado");
        }

        Role gestorRole = roleRepository.findByNombre("GESTOR");
        if (gestorRole == null) {
            throw new RuntimeException("Error: El rol GESTOR no existe en la base de datos");
        }

        User user = new User();
        user.setNombre(request.getNombre());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(gestorRole);

        userRepository.save(user);

        return new RegisterResponse("Gestor registrado exitosamente", user.getEmail());
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        String token = jwtUtil.generateToken(userDetails);

        return AuthResponse.builder()
                .token(token)
                .build();
    }
}
