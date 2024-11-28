package com.app.ecomisiones.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.ecomisiones.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByCorreoAndPassword(String dni, String password);
}
