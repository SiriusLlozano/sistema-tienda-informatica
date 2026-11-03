package com.example.tienda_computadoras.repository;

import com.example.tienda_computadoras.model.MovimientoInventario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovimientoInventarioRepository extends JpaRepository<MovimientoInventario, Long> {
}
