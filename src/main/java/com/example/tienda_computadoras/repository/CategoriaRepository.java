package com.example.tienda_computadoras.repository;

import com.example.tienda_computadoras.model.Categoria;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    Optional<Categoria> findFirstByNombreCategoriaIgnoreCase(String NombreCategoria);
}
