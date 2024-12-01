package com.app.ecomisiones.repository;

import com.app.ecomisiones.model.OrdenDeCompra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdenDeCompraRepository extends JpaRepository<OrdenDeCompra, Long> {
}
