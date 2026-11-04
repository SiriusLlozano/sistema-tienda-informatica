package com.example.tienda_computadoras.repository;

import com.example.tienda_computadoras.model.DetalleCompra;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DetalleCompraRepository extends JpaRepository<DetalleCompra, Long> {
    boolean existsByProducto_Id(Long productoId);
}
