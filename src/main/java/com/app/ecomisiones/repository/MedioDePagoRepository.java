package com.app.ecomisiones.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.app.ecomisiones.model.MedioDePago;

/**
 * Repositorio para acceder y gestionar la entidad {@link MedioDePago} en la base de datos.
 * Extiende de {@link JpaRepository}, proporcionando métodos CRUD básicos para la entidad {@link MedioDePago}.
 */
@Repository
public interface MedioDePagoRepository extends JpaRepository<MedioDePago, Integer> {

    /**
     * Encuentra todos los medios de pago que no estén marcados como inactivos (baja).
     * 
     * @return Una lista de {@link MedioDePago} que no están inactivos.
     */
    List<MedioDePago> findByBajaFalse();
}
