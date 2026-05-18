package com.nelumbo.reservas.dto.response.indicadores;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class TopClientes {
    private String documentoCliente;
    private String nombreCliente;
    private Long totalReservas;
}
