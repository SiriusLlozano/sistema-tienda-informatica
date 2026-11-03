package com.example.tienda_computadoras.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String modelo;
    private String sku;
    private BigDecimal precio;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    @ManyToOne
    @JoinColumn(name = "marca_id")
    private Marca marca;

    public Producto(String nombre, Categoria categoria, String modelo) {
        this.nombre = nombre;
        this.categoria = categoria;
        this.modelo = modelo;
        this.precio = BigDecimal.ZERO;
    }
}
