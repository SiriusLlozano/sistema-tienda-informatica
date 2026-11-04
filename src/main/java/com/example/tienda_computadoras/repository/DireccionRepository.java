package com.example.tienda_computadoras.repository;

import com.example.tienda_computadoras.model.Direccion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DireccionRepository extends JpaRepository<Direccion, Long> {
    boolean existsByCliente_Id(Long clienteId);
}
