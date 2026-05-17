package com.nelumbo.reservas.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FinalizarReservaRequest {
    
    @NotBlank(message = "El documento del cliente es obligatorio")
    @Pattern(regexp = "^\\d{6,12}$", message = "El documento debe tener entre 6 y 12 caracteres numéricos")
    private String documentoCliente;

    @NotNull(message = "El ID del salón es obligatorio")
    private Integer salonId;
}
