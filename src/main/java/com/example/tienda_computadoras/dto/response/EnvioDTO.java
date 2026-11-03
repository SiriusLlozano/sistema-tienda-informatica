package com.example.tienda_computadoras.dto.response;

import java.time.LocalDate;

public record EnvioDTO(
        Long id,
        String transportista,
        String numeroSeguimiento,
        String estado,
        LocalDate fechaEnvio,
        LocalDate fechaEntrega,
        Long ventaId,
        Long direccionId) {
}
