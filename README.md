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

### Registrar un cliente

`POST /clientes` recibe los datos básicos y responde con `ClienteDTO` y estado
`201 Created`:

```json
{
  "nombre": "Pedro",
  "correo": "pedro@ejemplo.com",
  "telefono": "999123456"
}
```

El correo se compara sin distinguir mayúsculas y el teléfono después de quitar
espacios al inicio y al final. Si alguno ya pertenece a otro cliente, responde
`409 Conflict`; el nombre sí puede repetirse.

Para listar los clientes registrados, usa `GET /clientes`. Responde una lista de
objetos `ClienteDTO` y requiere autenticación como `ADMIN` o `VENDEDOR`.

### CRUD de clientes

- `POST /clientes`: registra un cliente (ADMIN o VENDEDOR).
- `GET /clientes`: lista clientes (ADMIN o VENDEDOR).
- `GET /clientes/{clienteId}`: consulta un cliente (ADMIN o VENDEDOR).
- `PUT /clientes/{clienteId}`: reemplaza sus datos (ADMIN o VENDEDOR).
- `DELETE /clientes/{clienteId}`: elimina un cliente solo si no tiene ventas ni
  direcciones asociadas (solo ADMIN). Si hay relaciones, responde `409 Conflict`
  para conservar el historial.

El cuerpo de `PUT /clientes/{clienteId}` contiene todos los campos editables:

```json
{
  "nombre": "Pedro actualizado",
  "correo": "pedro.actualizado@ejemplo.com",
  "telefono": "999123456"
}
```

### CRUD de productos

- `POST /productos`: registra un producto (solo ADMIN).
- `GET /productos`: lista productos persistidos (ADMIN o VENDEDOR).
- `GET /productos/{productoId}`: consulta un producto por ID (ADMIN o VENDEDOR).
- `PUT /productos/{productoId}`: reemplaza sus datos editables (solo ADMIN).
- `DELETE /productos/{productoId}`: elimina un producto solo si no aparece en
  ventas, compras, inventario ni asociaciones con proveedores (solo ADMIN).
  Si tiene dependencias, responde `409 Conflict` para proteger el historial.

El cuerpo de `PUT /productos/{productoId}` contiene:

```json
{
  "nombre": "Laptop Lenovo",
  "categoria": "Portatiles",
  "modelo": "LOQ",
  "precio": 2500
}
```

La ruta anterior `GET /productos` devolvía datos fijos de ejemplo; ahora muestra
los productos guardados en la base de datos. `GET /listar` se conserva como ruta
legada para consultar las entidades Producto.

El historial de compras de un cliente se consulta con
`GET /clientes/{clienteId}/ventas`.

### Consultas de ventas

- `GET /ventas`: todas las ventas (solo `ADMIN`).
- `GET /ventas/{ventaId}`: detalle de una venta (solo `ADMIN`).
- `GET /productos/{productoId}/ventas`: historial de ventas que incluyen el
  producto (ADMIN o VENDEDOR).
- `GET /clientes/{clienteId}/ventas`: historial de compras del cliente
  (ADMIN o VENDEDOR).

### Registrar un pedido

`POST /pedido` recibe un JSON:

```json
{
  "tipo": "Delivery"
}
```

### Registrar una venta

Consulta primero `GET /listar` para encontrar el identificador de un producto.
Luego envía la venta con:

`POST /vender` recibe un JSON:

```json
{
  "cliente": "Pedro",
  "cantidad": 1,
  "productoId": 1
}
```

Ambos endpoints requieren autenticación como `ADMIN` o `VENDEDOR`. Actualmente,
`POST /pedido` solo devuelve un mensaje simulado; todavía no persiste un pedido
en la base de datos.

Para enviar JSON, usa Swagger UI o Postman: una URL `POST` no se prueba pegando
el JSON en la barra de direcciones del navegador. En Postman selecciona
`Body > raw > JSON`; en Swagger, abre el endpoint, pulsa `Try it out` e ingresa
el JSON en el campo del cuerpo. Para las rutas protegidas autentícate con un
usuario y contraseña configurados para la aplicación.
