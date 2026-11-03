# Sistema de tienda de computadoras

Proyecto de aprendizaje desarrollado con Java 21, Spring Boot, Spring Data JPA,
Spring Security y H2. La base H2 está configurada en memoria para desarrollo.

## Requisitos

- JDK 21
- Maven 3.6.3 o posterior (o Maven Wrapper)

## Configuración

La aplicación requiere cuatro variables de entorno para crear los usuarios de
prueba con roles `ADMIN` y `VENDEDOR`:

- `APP_ADMIN_USERNAME`
- `APP_ADMIN_PASSWORD`
- `APP_VENDEDOR_USERNAME`
- `APP_VENDEDOR_PASSWORD`

Configúralas en el entorno local antes de iniciar la aplicación. No guardes
contraseñas en este repositorio.

En PowerShell, por ejemplo:

```powershell
$env:APP_ADMIN_USERNAME = "admin"
$env:APP_ADMIN_PASSWORD = "<contraseña-local>"
$env:APP_VENDEDOR_USERNAME = "vendedor"
$env:APP_VENDEDOR_PASSWORD = "<contraseña-local>"
.\mvnw.cmd spring-boot:run
```

La aplicación usa una base de datos H2 en memoria, por lo que los datos se
reinician al detenerla.

## API

Con la aplicación iniciada, la documentación OpenAPI está disponible en:

- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`
