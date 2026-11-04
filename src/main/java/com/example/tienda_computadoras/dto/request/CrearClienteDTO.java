package com.example.tienda_computadoras.dto.request;

public record CrearClienteDTO(
        String nombre,
        String correo,
        String telefono
) {
}
