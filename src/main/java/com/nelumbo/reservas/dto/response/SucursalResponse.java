package com.nelumbo.reservas.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SucursalResponse {
    private Integer id;
    private String nombre;
    private String direccion;
    private String nombreGestor;
    private String emailGestor;
}
