package com.example.tienda_computadoras.repository;

import com.example.tienda_computadoras.model.ProductoProveedor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoProveedorRepository extends JpaRepository<ProductoProveedor, Long> {
    boolean existsByProducto_Id(Long productoId);
}
