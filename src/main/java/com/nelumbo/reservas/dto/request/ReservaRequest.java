package com.nelumbo.reservas.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservaRequest {

    @NotBlank(message = "El documento del cliente es obligatorio")
    @Pattern(regexp = "^\\d{6,12}$", message = "El documento debe tener entre 6 y 12 caracteres numéricos")
    private String documentoCliente;

    @NotBlank(message = "El nombre del cliente es obligatorio")
    @Size(min = 2, message = "El nombre del cliente debe tener al menos 2 caracteres")
    private String nombreCliente;

    @NotNull(message = "El salón es obligatorio")
    private Integer salonId;

    @NotNull(message = "La fecha de inicio es obligatoria")
    @Future(message = "La fecha de inicio debe ser en el futuro")
    private LocalDateTime fechaInicio;

    @NotNull(message = "La fecha de fin estimada es obligatoria")
    @Future(message = "La fecha de fin debe ser en el futuro")
    private LocalDateTime fechaFinEstimada;

    @NotNull(message = "El número de asistentes es obligatorio")
    @Min(value = 1, message = "El número de asistentes debe ser al menos 1")
    private Integer asistentes;
}
