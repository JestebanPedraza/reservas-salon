package com.nelumbo.reservas.repository;

import com.nelumbo.reservas.entity.HistoricoReserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoricoReservaRepository extends JpaRepository<HistoricoReserva, Integer> {
}
