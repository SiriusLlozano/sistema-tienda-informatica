package com.example.tienda_computadoras.dto.request;

public record RegistrarVentaDTO(
        String cliente,
        int cantidad,
        Long productoId
) {
}
