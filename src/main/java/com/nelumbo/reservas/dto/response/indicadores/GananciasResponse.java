package com.nelumbo.reservas.dto.response.indicadores;

import com.nelumbo.reservas.enums.PeriodoTipo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GananciasResponse {
    Integer salonId;
    PeriodoTipo periodo;
    Double total;
}
