package com.example.tienda_computadoras.repository;

import com.example.tienda_computadoras.model.DetalleVenta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, Long> {
    boolean existsByProducto_Id(Long productoId);
}
