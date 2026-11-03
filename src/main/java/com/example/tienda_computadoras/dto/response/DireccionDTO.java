package com.example.tienda_computadoras.dto.response;

public record DireccionDTO(
        Long id,
        String calle,
        String ciudad,
        String provincia,
        String codigoPostal,
        String pais,
        Long clienteId) {
}
