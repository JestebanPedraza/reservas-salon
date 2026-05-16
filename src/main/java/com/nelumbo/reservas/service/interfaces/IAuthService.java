package com.nelumbo.reservas.service.interfaces;

import com.nelumbo.reservas.dto.request.LoginRequest;
import com.nelumbo.reservas.dto.request.RegisterRequest;
import com.nelumbo.reservas.dto.response.AuthResponse;
import com.nelumbo.reservas.dto.response.RegisterResponse;

public interface IAuthService {
    RegisterResponse registerGestor(RegisterRequest request);
    AuthResponse login(LoginRequest request);
}
