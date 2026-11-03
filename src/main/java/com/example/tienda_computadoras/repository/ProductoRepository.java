package com.example.tienda_computadoras.repository;

import com.example.tienda_computadoras.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

// El repositorio es el que habla directamente con la base de datos (guardar, buscar, listar)
public interface ProductoRepository extends JpaRepository<Producto, Long> {
}
