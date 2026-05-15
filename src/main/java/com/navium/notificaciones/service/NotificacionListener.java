package com.navium.notificaciones.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.navium.notificaciones.dto.AgendamientoMensajeDTO;

@Service
public class NotificacionListener {

    private final MessageConverter jsonMessageConverter;

    //Mismo nombre de la cola del microservicio Agendamiento
    private static final String QUEUE_NOTIFICACIONES = "navium.notificaciones.queue";

    //Inyección servicio de correos
    @Autowired
    private CorreoService correoService;

    NotificacionListener(MessageConverter jsonMessageConverter) {
        this.jsonMessageConverter = jsonMessageConverter;
    }

   @RabbitListener(queues = QUEUE_NOTIFICACIONES)
    public void recibirMensaje(AgendamientoMensajeDTO mensaje) {
        
        System.out.println("========================================");
        System.out.println("NUEVA NOTIFICACIÓN RECIBIDA (FORMATO JSON)");
        System.out.println("Destino: " + mensaje.getCorreoUsuario());
        System.out.println("========================================");
        
        try {

            //Obtenemos el estado para analizarlo
            String estado = mensaje.getEstadoAgendamiento();

            //Si el estado es nulo, o el estado inicial es "CREADO", enviamos CONFIRMACIÓN
            if (estado == null || estado.isEmpty() || estado.equalsIgnoreCase("Creado")) {
                correoService.enviarCorreoConfirmacion(
                    mensaje.getCorreoUsuario(),
                    mensaje.getPatenteCamion(),
                    mensaje.getHoraInicio()
                );
                System.out.println("Correo de confirmación enviado.");

            }else{
                //Si el estado es algo diferente, enviamos actualización
                correoService.enviarCorreoActualizacion(
                    mensaje.getCorreoUsuario(),
                    mensaje.getPatenteCamion(),
                    estado
            );
            System.out.println("Correo de actualización (" + estado + ") enviado.");
            }

        }catch (Exception e) {
            System.err.println("ERROR al procesar notificación: " + e.getMessage());
        }
    }
}