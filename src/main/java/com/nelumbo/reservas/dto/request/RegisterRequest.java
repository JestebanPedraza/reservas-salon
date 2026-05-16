package com.nelumbo.reservas.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 150)
    private String nombre;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Email debe ser válido")
    @Size(max = 100)
    private String email;

    @NotBlank(message = "El password es obligatorio")
    @Size(min = 4, max = 20)
    private String password;
}
