package com.example.tienda_computadoras.repository;

import com.example.tienda_computadoras.model.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {
}
