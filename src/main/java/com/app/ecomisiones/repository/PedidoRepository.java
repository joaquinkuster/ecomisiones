package com.app.ecomisiones.repository;

import com.app.ecomisiones.model.Pedido;
import com.app.ecomisiones.model.Usuario;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para acceder y gestionar la entidad {@link Pedido} en la base de datos.
 * Extiende de {@link JpaRepository}, proporcionando métodos CRUD básicos para la entidad {@link Pedido}.
 */
@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Integer> {

    /**
     * Encuentra todos los pedidos que no están marcados como inactivos (baja).
     * 
     * @return Una lista de {@link Pedido} que no están inactivos.
     */
    List<Pedido> findByBajaFalse();

    /**
     * Encuentra todos los pedidos realizados por un {@link Usuario} específico.
     * 
     * @param usuario El usuario que ha realizado los pedidos.
     * @return Una lista de {@link Pedido} realizados por el usuario.
     */
    List<Pedido> findByComprador(Usuario usuario);
}
