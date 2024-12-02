package com.app.ecomisiones.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.ecomisiones.model.Paquete;

@Repository
public interface PaqueteRepository extends JpaRepository<Paquete, Integer> {
    List<Paquete> findByBajaFalse();
}
