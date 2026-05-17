package com.nelumbo.reservas.dto.response;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalonResponse {
    
    private Integer id;
    private String nombre;
    private Integer capacidadMaxima;
    private BigDecimal costoHora;
    private String sucursal;
    private String nombreGestor;
}
