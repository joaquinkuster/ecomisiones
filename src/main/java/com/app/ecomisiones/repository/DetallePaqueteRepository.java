package com.app.ecomisiones.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.ecomisiones.model.DetallePaquete;

@Repository
public interface DetallePaqueteRepository extends JpaRepository<DetallePaquete, Integer> {
    List<DetallePaquete> findByBajaFalse();
}
