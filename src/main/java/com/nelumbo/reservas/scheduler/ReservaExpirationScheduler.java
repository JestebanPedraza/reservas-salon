package com.nelumbo.reservas.scheduler;

import com.nelumbo.reservas.service.interfaces.IReservaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReservaExpirationScheduler {

    private final IReservaService reservaService;

    // Se ejecuta cada hora para revisar expiraciones
    @Scheduled(cron = "0 0 * * * *")
    public void checkExpirations() {
        log.info("Iniciando tarea programada para expirar reservas pendientes de más de 48 horas");
        reservaService.expirarReservasPendientes();
    }
}
