package com.example.tienda_computadoras.dto.response;

public record ProveedorDTO(
        Long id,
        String nombre,
        String identificacionFiscal,
        String correo,
        String telefono) {
}
