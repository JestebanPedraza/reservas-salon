package com.nelumbo.reservas.service.interfaces;

import java.util.List;

import com.nelumbo.reservas.dto.request.SalonRequest;
import com.nelumbo.reservas.dto.response.SalonResponse;

public interface ISalonService {
    SalonResponse create(SalonRequest request);
    SalonResponse update(Integer id, SalonRequest request);
    SalonResponse findById(Integer id);
    List<SalonResponse> findAll();
    void delete(Integer id);
    List<SalonResponse> findBySucursal(Integer sucursalId);
}
