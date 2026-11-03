package com.example.tienda_computadoras.repository;

import com.example.tienda_computadoras.model.Marca;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MarcaRepository extends JpaRepository<Marca, Long> {
}
