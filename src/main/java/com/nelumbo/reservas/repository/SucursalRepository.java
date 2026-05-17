package com.nelumbo.reservas.repository;

import com.nelumbo.reservas.entity.Sucursal;
import com.nelumbo.reservas.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SucursalRepository extends JpaRepository<Sucursal, Integer> {
    List<Sucursal> findByGestor(User gestor);
    List<Sucursal> findByNombreContainingIgnoreCase(String nombre);
}
