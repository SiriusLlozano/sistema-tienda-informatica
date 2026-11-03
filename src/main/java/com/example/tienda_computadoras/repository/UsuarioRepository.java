package com.example.tienda_computadoras.repository;

import com.example.tienda_computadoras.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}
