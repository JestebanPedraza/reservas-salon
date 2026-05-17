package com.nelumbo.reservas.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nelumbo.reservas.dto.response.TopClientes;
import com.nelumbo.reservas.entity.Reserva;
import com.nelumbo.reservas.entity.enums.EstadoReserva;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Integer> {

    boolean existsByDocumentoClienteAndEstado(String documentoCliente, EstadoReserva estado);

    List<Reserva> findBySalonIdAndEstado(Integer salonId, EstadoReserva estado);

    List<Reserva> findByDocumentoClienteContainingAndEstado(String documento, EstadoReserva estado);

    List<Reserva> findByEstadoAndFechaCreacionBefore(EstadoReserva estado, LocalDateTime fecha);

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

    Optional<Reserva> findByDocumentoClienteAndSalonIdAndEstado(String documento, Integer salonId, EstadoReserva estado);

    @Query(value = "SELECT r.documento_cliente as documentoCliente, r.nombre_cliente as nombreCliente, COUNT(r.id) as totalReservas " +
                   "FROM reservas r GROUP BY r.documento_cliente, r.nombre_cliente " +
                   "ORDER BY totalReservas DESC LIMIT 10", nativeQuery = true)
    List<TopClientes> findTop10ClientesGeneral();

    @Query(value = "SELECT r.documento_cliente as documentoCliente, r.nombre_cliente as nombreCliente, COUNT(r.id) as totalReservas " +
                   "FROM reservas r WHERE r.salon_id = :salonId " +
                   "GROUP BY r.documento_cliente, r.nombre_cliente " +
                   "ORDER BY totalReservas DESC LIMIT 10", nativeQuery = true)
    List<TopClientes> findTop10ClientesBySalon(@Param("salonId") Integer salonId);
}
