package com.example.tienda_computadoras.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import com.example.tienda_computadoras.dto.response.DetalleCompraDTO;
import java.util.List;

public record CompraDTO(
        Long id,
        LocalDate fecha,
        String estado,
        BigDecimal total,
        Long proveedorId,
        Long empleadoId,
        Long sucursalId,
        List<DetalleCompraDTO> detalles) {
}
