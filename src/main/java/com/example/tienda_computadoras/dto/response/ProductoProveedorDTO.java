package com.example.tienda_computadoras.dto.response;

import java.math.BigDecimal;

public record ProductoProveedorDTO(
        Long id,
        BigDecimal costoCompra,
        String codigoProveedor,
        Long productoId,
        Long proveedorId) {
}
