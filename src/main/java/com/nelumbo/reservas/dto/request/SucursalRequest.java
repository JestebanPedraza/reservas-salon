package com.nelumbo.reservas.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SucursalRequest {
    
    @NotBlank(message = "El nombre de la sucursal es obligatorio")
    @Size(min = 2, message = "El nombre de la sucursal debe tener al menos 2 caracteres")
    private String nombre;
    
    @NotBlank(message = "La dirección de la sucursal es obligatoria")
    @Size(min = 5, message = "La dirección de la sucursal debe tener al menos 5 caracteres")
    private String direccion;

    @NotNull(message = "El ID del gestor es obligatorio")
    private Integer gestorId;
}
