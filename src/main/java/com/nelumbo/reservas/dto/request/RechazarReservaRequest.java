package com.nelumbo.reservas.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RechazarReservaRequest {
    @NotBlank(message = "El motivo de rechazo es obligatorio")
    private String motivo;
}
