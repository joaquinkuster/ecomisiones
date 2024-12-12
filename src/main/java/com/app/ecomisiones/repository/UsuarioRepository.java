package com.app.ecomisiones.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.app.ecomisiones.model.Usuario;

/**
 * Repositorio para acceder y gestionar la entidad {@link Usuario} en la base de datos.
 * Extiende de {@link JpaRepository}, proporcionando métodos CRUD básicos para la entidad {@link Usuario}.
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    /**
     * Encuentra un usuario por su correo y que no esté marcado como inactivo (baja).
     * 
     * @param correo El correo electrónico del usuario a buscar.
     * @return Un {@link Optional} que contiene el {@link Usuario} si se encuentra, o vacío si no se encuentra.
     */
    Optional<Usuario> findByCorreoAndBajaFalse(String correo);
}
