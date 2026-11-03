package com.example.tienda_computadoras.repository;

import com.example.tienda_computadoras.model.Inventario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventarioRepository extends JpaRepository<Inventario, Long> {
}
