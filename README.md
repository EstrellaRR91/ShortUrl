# ShortUrl
Backend URL shortening service built with Spring Boot, Redis for caching, MySQL for persistence, and secure JWT authentication. Includes IP rate limiting, RESTful APIs, and modular design

 ShortURL - Servicio de Acortamiento de URLs

Aplicación backend construida con Spring Boot para generar enlaces cortos desde URLs largas, con soporte para autenticación, estadísticas, y almacenamiento en Redis y MySQL.

 Características

- Acorta URLs automáticamente
- Control de rate-limit por IP
- Almacenamiento híbrido: Redis (cache) + MySQL (persistencia)
- Autenticación con JWT
- API RESTful estructurada
- Preparado para pruebas unitarias e integración

 Tecnologías Usadas

- Java 17
- Spring Boot 3.5.3
- Spring Data JPA + MySQL
- Spring Data Redis
- JWT (autenticación)
- Maven

 Configuración Local

1. Clonar el repositorio:

```bash

