package com.nelumbo.reservas.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalonRequest {
    
    @NotBlank(message = "El nombre del salón es obligatorio")
    @Size(min = 2, message = "El nombre del salón debe tener al menos 2 caracteres")
    private String nombre;

    @Positive(message = "La capacidad del salón debe ser un número positivo")
    @NotNull(message = "La capacidad del salón es obligatoria")
    private Integer capacidadMaxima;

    @Positive(message = "El costo por hora del salón debe ser un número positivo")
    @NotNull(message = "El costo por hora del salón es obligatorio")
    private BigDecimal costoHora;

    @NotNull(message = "El ID de la sucursal es obligatorio")
    private Integer sucursalId;
}
