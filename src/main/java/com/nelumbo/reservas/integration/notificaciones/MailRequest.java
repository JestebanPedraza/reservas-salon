package com.nelumbo.reservas.integration.notificaciones;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MailRequest {
    private String email;
    private String documento;
    private String mensaje;
    private String salonNombre;
}
