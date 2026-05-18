package com.nelumbo.reservas.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nelumbo.reservas.dto.response.TopSucursalesFacturacionResponse;
import com.nelumbo.reservas.entity.HistoricoReserva;

@Repository
public interface HistoricoReservaRepository extends JpaRepository<HistoricoReserva, Integer> {

    @Query("SELECT new com.nelumbo.reservas.dto.response.TopSucursalesFacturacionResponse(s.nombre, SUM(h.totalCobrado)) " +
           "FROM HistoricoReserva h " +
           "JOIN h.reserva r " +
           "JOIN r.salon sa " +
           "JOIN sa.sucursal s " +
           "WHERE h.fechaFinReal >= :desde " +
           "GROUP BY s.nombre " +
           "ORDER BY SUM(h.totalCobrado) DESC")
    List<TopSucursalesFacturacionResponse> findTopSucursales(@Param("desde") LocalDateTime desde, Pageable pageable);

    @Query("SELECT SUM(h.totalCobrado) FROM HistoricoReserva h " +
           "WHERE h.reserva.salon.id = :salonId " +
           "AND h.fechaFinReal BETWEEN :inicio AND :fin")
    Double sumGanancias(@Param("salonId") Integer salonId,
                        @Param("inicio") LocalDateTime inicio,
                        @Param("fin") LocalDateTime fin);
}
