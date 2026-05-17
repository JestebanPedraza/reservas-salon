package com.nelumbo.reservas.repository;

import com.nelumbo.reservas.entity.Salon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalonRepository extends JpaRepository<Salon, Integer> {
    List<Salon> findBySucursalId(Integer sucursalId);
}
