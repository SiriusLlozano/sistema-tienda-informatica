package com.example.tienda_computadoras.repository;

import com.example.tienda_computadoras.model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;

// El repositorio de ventas habla con la tabla "venta" de la base de datos
public interface VentaRepository extends JpaRepository<Venta, Long> {
}
