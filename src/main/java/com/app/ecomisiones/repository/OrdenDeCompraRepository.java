package com.app.ecomisiones.repository;

import com.app.ecomisiones.model.Orden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdenDeCompraRepository extends JpaRepository<Orden, Integer> {
}
