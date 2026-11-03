package com.example.tienda_computadoras.dto.response;

public record UsuarioDTO(Long id, String nombreUsuario, boolean activo, Long rolId, Long empleadoId) {
}
