package com.nelumbo.reservas.dto.response;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservaResponse {
    private Integer id;
    private String documentoCliente;
    private String nombreCliente;
    private Integer salonId;
    private String salonNombre;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFinEstimada;
    private Integer asistentes;
    private String estado;
    private BigDecimal totalEstimado;
}
