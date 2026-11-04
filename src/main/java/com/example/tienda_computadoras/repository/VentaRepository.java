package com.example.tienda_computadoras.repository;

import com.example.tienda_computadoras.model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

// El repositorio de ventas habla con la tabla "venta" de la base de datos
public interface VentaRepository extends JpaRepository<Venta, Long> {
    List<Venta> findDistinctByDetalles_Producto_IdOrderByFechaDesc(Long productoId);

    List<Venta> findByCliente_IdOrderByFechaDesc(Long clienteId);
}
