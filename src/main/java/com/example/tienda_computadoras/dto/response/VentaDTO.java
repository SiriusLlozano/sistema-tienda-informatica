package com.example.tienda_computadoras.dto.response;

import java.time.LocalDate;
import com.example.tienda_computadoras.dto.response.DetalleVentaDTO;
import java.util.List;

public record VentaDTO(
        Long id,
        LocalDate fecha,
        Long clienteId,
        Long empleadoId,
        Long sucursalId,
        List<DetalleVentaDTO> detalles) {
}
