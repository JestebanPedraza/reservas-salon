package com.nelumbo.reservas.integration.notificaciones;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.nelumbo.reservas.entity.Reserva;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Component
@Slf4j
@RequiredArgsConstructor
@Async
public class NotificacionHelper {

    private final RestTemplate restTemplate;

    @Value("${notificaciones.url}")
    private String notificationsUrl;

    // El try-catch se queda a vivir aquí, aislado del mundo
    public void enviarNotificacion(Reserva reserva, String mensaje) {
        try {
            String emailGestor = reserva.getSalon().getSucursal().getGestor().getEmail();
            MailRequest mailRequest = MailRequest.builder()
                    .email(emailGestor)
                    .documento(reserva.getDocumentoCliente())
                    .mensaje(mensaje)
                    .salonNombre(reserva.getSalon().getNombre())
                    .build();

            restTemplate.postForEntity(notificationsUrl + "/api/notificaciones", mailRequest, Void.class);
        } catch (Exception e) {
            log.error("Fallo silencioso al notificar: {}", e.getMessage());
        }
    }
}
