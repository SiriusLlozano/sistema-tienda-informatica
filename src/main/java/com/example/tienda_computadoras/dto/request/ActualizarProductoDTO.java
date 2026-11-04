package com.example.tienda_computadoras.dto.request;

import java.math.BigDecimal;

public record ActualizarProductoDTO(
        String nombre,
        String categoria,
        String modelo,
        BigDecimal precio
) {
}
