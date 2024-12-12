package com.app.ecomisiones.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.app.ecomisiones.model.DetallePedido;

/**
 * Repositorio para acceder y gestionar la entidad {@link DetallePedido} en la base de datos.
 * Extiende de {@link JpaRepository}, proporcionando métodos CRUD básicos para la entidad {@link DetallePedido}.
 */
@Repository
public interface DetallePedidoRepository extends JpaRepository<DetallePedido, Integer> {

    /**
     * Busca los detalles de pedido que no están marcados como inactivos.
     * Este método permite recuperar una lista de detalles de pedido cuyo campo {@code baja} es {@code false}.
     * 
     * @return Una lista de detalles de pedido activos (no inactivos).
     */
    List<DetallePedido> findByBajaFalse();
}
