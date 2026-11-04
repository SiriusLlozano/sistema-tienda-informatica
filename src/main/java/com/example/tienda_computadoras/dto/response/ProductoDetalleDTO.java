package com.example.tienda_computadoras.dto.response;

import java.math.BigDecimal;

public record ProductoDetalleDTO(
        Long id,
        String nombre,
        String categoria,
        String modelo,
        BigDecimal precio
) {
}
