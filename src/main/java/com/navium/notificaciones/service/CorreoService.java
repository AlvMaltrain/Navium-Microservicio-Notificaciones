package com.navium.notificaciones.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class CorreoService {

    @Autowired
    private JavaMailSender mailSender;

    public void enviarCorreoConfirmacion(String destinatario, String patente, String hora) {
        SimpleMailMessage mensaje = new SimpleMailMessage();
        
        // Quién lo envía 
        mensaje.setFrom("avlaromaltrain@gmail.com"); 
        
        // A quién va dirigido
        mensaje.setTo(destinatario); 
        
        // Asunto del correo
        mensaje.setSubject("Confirmación de Ingreso al Puerto - Patente: " + patente); 
        
        // Cuerpo del correo
        mensaje.setText("Hola,\n\nSe ha confirmado exitosamente el agendamiento del camión con patente " 
                        + patente + " para el bloque horario de las " + hora + "."
                        +"\nLe mantendremos informado sobre cualquier actualización relacionada con su agendamiento por este medio.\n\n"
                        +"\nSistema Portuario Navium.");
        
        // Enviar
        mailSender.send(mensaje);
        
        System.out.println("Correo enviado exitosamente a: " + destinatario);
    }

    public void enviarCorreoActualizacion(String destinatario, String patente, String estado) {
        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setFrom("avlaromaltrain@gmail.com");
        mensaje.setTo(destinatario);
        mensaje.setSubject("Actualización de Estado - Patente: " + patente);

        mensaje.setText("Estimado cliente, \n\nLe informamos que la operación del camión con patente: "
                        + patente + " ha cambiado su estado a: " + estado + ".\n\n"
                        + "Le mantendremos informado sobre cualquier actualización relacionada con su agendamiento por este medio.\n\n");

        mailSender.send(mensaje);
        System.out.println("Correo de actualización enviado exitosamente a: " + destinatario);
    }
}