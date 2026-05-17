import { Body, Controller, Get, HttpCode, HttpStatus, Logger, Post } from '@nestjs/common';
import { NotificacionRequestDto } from './app.dto';

@Controller('api/notificaciones')
export class AppController {
  private readonly logger = new Logger(AppController.name);

  @Post()
  @HttpCode(HttpStatus.OK)
  enviarNotificacion(@Body()dto: NotificacionRequestDto) {
    this.logger.log('====== SOLICITUD DE NOTIFICACIÓN RECIBIDA ======');
    this.logger.log(`Email: ${dto.email}`);
    this.logger.log(`Documento: ${dto.documento}`);
    this.logger.log(`Mensaje: ${dto.mensaje}`);
    this.logger.log(`Salón Nombre: ${dto.salonNombre}`);
    this.logger.log('================================================');

    return {
      mensaje: 'Notificación Enviada',
    };
  }
}
