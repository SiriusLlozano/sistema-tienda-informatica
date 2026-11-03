package com.example.tienda_computadoras.dto.request;

public record CrearUsuarioDTO(
        String nombreUsuario,
        String contrasena,
        boolean activo,
        Long rolId,
        Long empleadoId) {
}
