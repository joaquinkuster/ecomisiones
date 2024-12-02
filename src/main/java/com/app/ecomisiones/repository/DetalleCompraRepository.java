package com.app.ecomisiones.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.ecomisiones.model.DetalleCompra;

@Repository
public interface DetalleCompraRepository extends JpaRepository<DetalleCompra, Integer> {
    List<DetalleCompra> findByBajaFalse();
}
