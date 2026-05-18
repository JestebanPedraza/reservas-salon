package com.nelumbo.reservas.dto.response;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TopSucursalesFacturacionResponse {
    private String sucursalNombre;
    private BigDecimal facturacion;
}
