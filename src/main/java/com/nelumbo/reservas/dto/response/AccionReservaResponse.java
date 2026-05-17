package com.nelumbo.reservas.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccionReservaResponse {
    private String mensaje;
    private LocalDateTime fecha;
}
