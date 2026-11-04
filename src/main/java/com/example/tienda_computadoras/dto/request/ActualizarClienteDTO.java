package com.example.tienda_computadoras.dto.request;

public record ActualizarClienteDTO(
        String nombre,
        String correo,
        String telefono
) {
}
