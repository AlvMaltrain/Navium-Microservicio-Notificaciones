# Notificaciones

Servicio Spring Boot para envío de notificaciones por correo mediante mensajes recibidos desde RabbitMQ.

## Descripción

Este microservicio escucha la cola `navium.notificaciones.queue` en RabbitMQ y envía mensajes de correo según el estado de un agendamiento recibido.

- Si el estado del agendamiento es `CREADO` o está vacío, envía un correo de confirmación.
- Si el estado es diferente, envía un correo de actualización.

## Tecnologías

- Java 17
- Spring Boot 4
- Spring AMQP
- Spring Mail
- RabbitMQ

## Estructura principal

- `NotificacionesApplication.java` - punto de entrada Spring Boot
- `NotificacionListener.java` - escucha la cola RabbitMQ y procesa mensajes
- `CorreoService.java` - envía correos usando `JavaMailSender`
- `AgendamientoMensajeDTO.java` - modelo de datos para mensajes JSON entrantes
- `application.properties` - configuración de RabbitMQ, servidor web y correo

## Configuración

1. Ajusta los valores en `src/main/resources/application.properties`:
   - `server.port` si necesitas un puerto distinto.
   - `spring.rabbitmq.addresses` para apuntar a tu servidor RabbitMQ.
   - `spring.mail.host`, `spring.mail.port`, `spring.mail.username`, `spring.mail.password` para configurar el servidor SMTP.

> Nota: actualmente las credenciales de correo están en texto plano en `application.properties`. Para un entorno real, usa variables de entorno o un gestor de secretos.

## Uso

Desde la carpeta del proyecto con el `pom.xml`:

```bash
./mvnw clean package
./mvnw spring-boot:run
```

O ejecuta el JAR generado:

```bash
java -jar target/notificaciones-0.0.1-SNAPSHOT.jar
```

## Comportamiento esperado

- El servicio arranca en el puerto `8083`.
- Se conecta al broker RabbitMQ usando la URL configurada.
- Recibe objetos JSON convertidos a `AgendamientoMensajeDTO`.
- Envía un correo de confirmación o actualización según el estado del agendamiento.

## Cola RabbitMQ

Escucha en la cola:

- `navium.notificaciones.queue`

Asegúrate de que el microservicio que publica mensajes use la misma cola y formato JSON.

## Mejoras sugeridas

- Externalizar las credenciales SMTP a variables de entorno.
- Añadir pruebas unitarias para `NotificacionListener` y `CorreoService`.
- Añadir manejo de errores más detallado y reintentos en caso de fallo de envío.
- Configurar `spring.mail.properties.mail.smtp.starttls.enable=true` y autenticación seguros para producción.
