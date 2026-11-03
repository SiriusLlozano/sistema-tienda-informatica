package com.example.tienda_computadoras.dto.response;

public record InventarioDTO(Long id, int existencia, int stockMinimo, Long productoId, Long sucursalId) {
}
