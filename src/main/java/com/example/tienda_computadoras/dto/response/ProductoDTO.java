package com.example.tienda_computadoras.dto.response;

import com.example.tienda_computadoras.model.Categoria;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductoDTO {
    private String nombre;
    private String categoria;

    public ProductoDTO(String nombre, Categoria categoria) {
        this.nombre = nombre;
        this.categoria = categoria == null ? null : categoria.getNombreCategoria();
    }
}
