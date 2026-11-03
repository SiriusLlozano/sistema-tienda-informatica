package com.example.tienda_computadoras.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PagoDTO(
        Long id,
        BigDecimal monto,
        LocalDateTime fecha,
        String referencia,
        Long ventaId,
        Long metodoPagoId) {
}
