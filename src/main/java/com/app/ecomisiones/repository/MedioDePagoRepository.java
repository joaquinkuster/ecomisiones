package com.app.ecomisiones.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.ecomisiones.model.MedioDePago;

@Repository
public interface MedioDePagoRepository extends JpaRepository<MedioDePago, Integer> {
    List<MedioDePago> findByBajaFalse();
}
