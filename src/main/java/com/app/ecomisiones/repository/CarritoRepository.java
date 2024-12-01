package com.app.ecomisiones.repository;

import com.app.ecomisiones.model.Carrito;
import com.app.ecomisiones.model.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarritoRepository extends JpaRepository<Carrito, Integer> {
    Carrito findByUsuario(Usuario usuario);
}
