package com.app.ecomisiones.repository;

import com.app.ecomisiones.model.Carrito;
import com.app.ecomisiones.model.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para acceder y gestionar la entidad {@link Carrito} en la base de datos.
 * Extiende de {@link JpaRepository}, proporcionando métodos CRUD básicos para la entidad {@link Carrito}.
 */
@Repository
public interface CarritoRepository extends JpaRepository<Carrito, Integer> {

    /**
     * Busca un carrito asociado a un usuario específico.
     * Este método permite obtener el carrito de un usuario dado, si existe.
     * 
     * @param usuario El usuario cuyo carrito se desea recuperar.
     * @return El carrito asociado al usuario especificado, o {@code null} si no existe un carrito para el usuario.
     */
    Carrito findByUsuario(Usuario usuario);
}
