package com.nelumbo.reservas.service.interfaces;

import com.nelumbo.reservas.dto.request.SucursalRequest;
import com.nelumbo.reservas.dto.response.SucursalResponse;

import java.util.List;

public interface ISucursalService {
    SucursalResponse create(SucursalRequest request);
    SucursalResponse update(Integer id, SucursalRequest request);
    SucursalResponse findById(Integer id);
    List<SucursalResponse> findAll();
    void delete(Integer id);
}
