package com.example.tienda_computadoras.dto.response;

import java.time.LocalDateTime;

public record MovimientoInventarioDTO(
        Long id,
        String tipo,
        int cantidad,
        String motivo,
        LocalDateTime fecha,
        Long inventarioId) {
}
