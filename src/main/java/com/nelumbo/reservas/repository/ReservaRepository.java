package com.nelumbo.reservas.repository;

import com.nelumbo.reservas.entity.Reserva;
import com.nelumbo.reservas.entity.enums.EstadoReserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Integer> {

    boolean existsByDocumentoClienteAndEstado(String documentoCliente, EstadoReserva estado);

    List<Reserva> findBySalonIdAndEstado(Integer salonId, EstadoReserva estado);

    List<Reserva> findByDocumentoClienteContainingAndEstado(String documento, EstadoReserva estado);

    @Query("SELECT SUM(r.asistentes) FROM Reserva r " +
           "WHERE r.salon.id = :salonId " +
           "AND r.estado = :estado " +
           "AND ((:inicio BETWEEN r.fechaInicio AND r.fechaFinEstimada) " +
           "OR (:fin BETWEEN r.fechaInicio AND r.fechaFinEstimada) " +
           "OR (r.fechaInicio BETWEEN :inicio AND :fin))")
    Integer sumAsistentesBySalonInRange(@Param("salonId") Integer salonId, 
                                        @Param("inicio") LocalDateTime inicio, 
                                        @Param("fin") LocalDateTime fin,
                                        @Param("estado") EstadoReserva estado);

    // Buscar reserva activa por documento y salón
    Optional<Reserva> findByDocumentoClienteAndSalonIdAndEstado(String documento, Integer salonId, EstadoReserva estado);
}
