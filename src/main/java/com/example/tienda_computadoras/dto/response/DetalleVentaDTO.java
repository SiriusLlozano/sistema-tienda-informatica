package com.example.tienda_computadoras.dto.response;

import java.math.BigDecimal;

public record DetalleVentaDTO(Long id, int cantidad, BigDecimal precioUnitario, Long productoId) {
}
